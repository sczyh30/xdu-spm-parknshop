package io.spm.parknshop.refund.service.impl;

import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.payment.domain.PaymentRefundResult;
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

  @Override
  public Mono<RefundRecord> saveRefundCompleteInfo(Long refundId, PaymentRefundResult refundResult) {
    if (Objects.isNull(refundId) || refundId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("bad refund request"));
    }
    return getRefundRecordById(refundId)
      .map(refundRecord -> refundRecord.setRefundStatus(RefundStatus.REFUND_SUCCESSFUL).setRefundTime(new Date()).setRefundTransactionNo(refundResult.getRefundNo()))
      .flatMap(this::saveInternal);
  }

  @Override
  public Mono<RefundRecord> createRefundRecord(Long subOrderId, String requestComment) {
    return checkIfRefundInProgressOrCompleted(subOrderId)
      .flatMap(v -> async(() -> buildRefundRecord(subOrderId, requestComment)))
      .flatMap(this::saveInternal);
  }

  private Mono<RefundRecord> saveInternal(/*@Normal*/ RefundRecord refundRecord) {
    return async(() -> refundRecordRepository.save(refundRecord));
  }

  private Mono<RefundRecord> checkIfRefundInProgressOrCompleted(Long subOrderId) {
    Set<Integer> set = Stream.of(RefundStatus.APPROVED_WAIT_RETURN, RefundStatus.NEW_CREATED,
      RefundStatus.REFUND_SUCCESSFUL, RefundStatus.PENDING_REFUND_TRANSACTION).collect(Collectors.toSet());
    return getCurrentRefundRecordBySubOrderId(subOrderId)
      .flatMap(refundRecord -> {
        if (set.contains(refundRecord.getRefundStatus())) {
          return Mono.error(new ServiceException(REFUND_ALREADY_STARTED, "Refund has already started or completed"));
        } else {
          return Mono.just(refundRecord);
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
    return asyncExecute(() -> refundRecordRepository.updateWithResponseAndStatus(RefundStatus.REJECTED, responseComment, id));
  }

  @Override
  public Mono<Long> withdrawRefundRequest(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("bad refund request"));
    }
    return asyncExecute(() -> refundRecordRepository.updateWithResponseAndStatus(RefundStatus.CANCELED, null, id));
  }

  @Override
  public Mono<RefundRecord> getRefundRecordById(Long refundId) {
    return async(() -> refundRecordRepository.findById(refundId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(REFUND_NOT_EXIST, "The refund record does not exist")));
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
}
