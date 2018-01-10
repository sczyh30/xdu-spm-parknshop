package io.spm.parknshop.refund.service.impl;

import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.order.domain.OrderEventType;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.domain.SubOrderStatus;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.order.service.OrderService;
import io.spm.parknshop.payment.domain.PaymentRefundResult;
import io.spm.parknshop.payment.service.PaymentService;
import io.spm.parknshop.refund.domain.RefundRecord;
import io.spm.parknshop.refund.domain.RefundStatus;
import io.spm.parknshop.refund.repository.RefundRecordRepository;
import io.spm.parknshop.refund.service.RefundDataService;
import io.spm.parknshop.refund.service.RefundService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class RefundServiceImpl implements RefundService, RefundDataService {

  @Autowired
  private RefundRecordRepository refundRecordRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderProductRepository orderProductRepository;

  @Autowired
  private PaymentService paymentService;
  @Autowired
  private OrderService orderService;

  @Override
  public Mono<RefundRecord> startRefundProcess(Long refundId) {
    if (Objects.isNull(refundId) || refundId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("bad refund request"));
    }
    return getRefundRecordById(refundId)
      .flatMap(this::checkProcessRefundStatus)
      .flatMap(refundRecord -> paymentService.processRefund(refundRecord.getBuyPaymentId(), generateRefundNo(refundRecord),
        refundRecord.getRefundAmount(), refundRecord.getStoreId()))
      .flatMap(refundResult -> saveRefundCompleteInfo(refundId, refundResult));
  }

  private Mono<RefundRecord> saveSubOrderRefundStatus(RefundRecord refundRecord) {
    return asyncExecute(() -> orderProductRepository.updateStatus(refundRecord.getSubOrderId(), SubOrderStatus.ALREADY_REFUND))
      .map(e -> refundRecord);
  }

  private Mono<RefundRecord> checkProcessRefundStatus(RefundRecord refundRecord) {
    if (refundRecord.getRefundStatus() != RefundStatus.APPROVED_WAIT_RETURN) {
      return Mono.error(new ServiceException(REFUND_INVALID_OPERATION, "Invalid refund operation"));
    }
    return Mono.just(refundRecord);
  }

  private String generateRefundNo(RefundRecord refundRecord) {
    return String.format("RF%10d%08d%08d", System.currentTimeMillis(), refundRecord.getOrderId(), refundRecord.getSubOrderId());
  }

  private Mono<RefundRecord> saveRefundCompleteInfo(Long refundId, PaymentRefundResult refundResult) {
    if (Objects.isNull(refundId) || refundId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("bad refund request"));
    }
    return getRefundRecordById(refundId)
      .map(refundRecord -> refundRecord.setRefundStatus(RefundStatus.REFUND_SUCCESSFUL).setRefundTime(new Date()).setRefundTransactionNo(refundResult.getRefundNo()))
      .flatMap(this::tryModifyOrderThenSaveInternal);
  }

  private Mono<RefundRecord> tryModifyOrderThenSaveInternal(/*@Normal*/ RefundRecord refundRecord) {
    long orderId = refundRecord.getOrderId();
    return async(() -> {
      RefundRecord newRecord = refundRecordRepository.save(refundRecord);
      orderProductRepository.updateStatus(newRecord.getSubOrderId(), SubOrderStatus.ALREADY_REFUND);
      return newRecord;
    }).flatMap(record -> modifySingleOrderStatus(orderId).map(e -> record));
  }

  @Override
  public Mono<RefundRecord> createRefundRecord(Long subOrderId, String requestComment) {
    return checkIfRefundInProgressOrCompleted(subOrderId)
      .flatMap(v -> async(() -> buildRefundRecord(subOrderId, requestComment)))
      .flatMap(this::saveNewInternal);
  }

  private Mono<RefundRecord> saveNewInternal(/*@Normal*/ RefundRecord refundRecord) {
    return async(() -> {
      RefundRecord newRecord = refundRecordRepository.save(refundRecord);
      orderProductRepository.updateStatus(newRecord.getSubOrderId(), SubOrderStatus.REFUND_IN_PROGRESS);
      return newRecord;
    });
  }

  private Mono<?> modifySingleOrderStatus(long orderId) {
    return async(() -> orderProductRepository.countByOrderIdWithNonRefunded(orderId))
      .flatMap(count -> {
        if (count <= 0) {
          return orderService.modifyOrderStatus(orderId, new OrderEvent(orderId, OrderEventType.REFUND_COMPLETE));
        }
        return Mono.just(0L);
      });
  }

  private Mono<?> checkIfRefundInProgressOrCompleted(Long subOrderId) {
    Set<Integer> set = Stream.of(RefundStatus.APPROVED_WAIT_RETURN, RefundStatus.NEW_CREATED,
      RefundStatus.REFUND_SUCCESSFUL, RefundStatus.PENDING_REFUND_TRANSACTION).collect(Collectors.toSet());
    return getCurrentRefundRecord(subOrderId)
      .flatMap(opt -> {
        if (opt.isPresent()) {
          RefundRecord refundRecord = opt.get();
          if (set.contains(refundRecord.getRefundStatus())) {
            return Mono.error(new ServiceException(REFUND_ALREADY_STARTED, "Refund has already started or completed"));
          } else {
            return Mono.just(refundRecord);
          }
        } else {
          return Mono.just(0L);
        }
      });
  }

  @Transactional(readOnly = true)
  protected RefundRecord buildRefundRecord(Long subOrderId, String requestComment) {
    OrderProduct subOrder = orderProductRepository.findById(subOrderId).get();
    long orderId = subOrder.getOrderId();
    Order order = orderRepository.findById(orderId).get();
    return new RefundRecord().setBuyPaymentId(order.getPaymentId().toString())
      .setCustomerId(order.getCreatorId())
      .setOrderId(orderId)
      .setStoreId(order.getStoreId())
      .setSubOrderId(subOrderId)
      .setRefundAmount(subOrder.getTotalPrice())
      .setRefundRequestMessage(requestComment)
      .setRefundStatus(RefundStatus.NEW_CREATED);
  }

  @Override
  public Mono<Long> approveRefundRequest(Long id, String responseComment) {
    if (StringUtils.isEmpty(responseComment)) {
      return Mono.error(ExceptionUtils.invalidParam("response comment"));
    }
    return asyncExecute(() -> refundRecordRepository.updateWithResponseAndStatus(RefundStatus.APPROVED_WAIT_RETURN, responseComment, id));
  }

  @Override
  public Mono<Long> rejectRefundRequest(Long id, String responseComment) {
    if (StringUtils.isEmpty(responseComment)) {
      return Mono.error(ExceptionUtils.invalidParam("response comment"));
    }
    return asyncExecute(() -> {
      refundRecordRepository.updateWithResponseAndStatus(RefundStatus.REJECTED, responseComment, id);
      refundRecordRepository.findById(id).ifPresent(record ->
        orderProductRepository.updateStatus(record.getSubOrderId(), SubOrderStatus.NORMAL));
    });
  }

  @Override
  public Mono<Long> withdrawRefundRequest(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("bad refund request"));
    }
    return asyncExecute(() -> {
      refundRecordRepository.updateWithResponseAndStatus(RefundStatus.CANCELED, null, id);
      refundRecordRepository.findById(id).ifPresent(record ->
        orderProductRepository.updateStatus(record.getSubOrderId(), SubOrderStatus.NORMAL));
    });
  }

  @Override
  public Mono<RefundRecord> getRefundRecordById(Long refundId) {
    return async(() -> refundRecordRepository.findById(refundId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(REFUND_NOT_EXIST, "The refund record does not exist")));
  }

  private Mono<Optional<RefundRecord>> getCurrentRefundRecord(Long subOrderId) {
    return async(() -> refundRecordRepository.getCurrentRefundBySubOrderId(subOrderId));
  }

  @Override
  public Mono<RefundRecord> getCurrentRefundRecordBySubOrderId(Long subOrderId) {
    return async(() -> refundRecordRepository.getCurrentRefundBySubOrderId(subOrderId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(REFUND_NOT_EXIST, "The order product does not have related refund record")));
  }

  @Override
  public Flux<RefundRecord> getRefundRecordByOrderId(Long orderId) {
    return asyncIterable(() -> refundRecordRepository.getByOrderId(orderId));
  }

  @Override
  public Flux<RefundRecord> getRefundRecordByStoreId(Long storeId) {
    return asyncIterable(() -> refundRecordRepository.getAllByStoreIdOrderByIdDesc(storeId));
  }

  @Override
  public Flux<RefundRecord> getRefundRecordByUserId(Long userId) {
    return asyncIterable(() -> refundRecordRepository.getAllByCustomerIdOrderByIdDesc(userId));
  }
}
