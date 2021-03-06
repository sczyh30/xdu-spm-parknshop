package io.spm.parknshop.payment.service.impl;

import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.order.service.OrderService;
import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.payment.domain.PaymentRefundResult;
import io.spm.parknshop.payment.domain.PaymentStatus;
import io.spm.parknshop.payment.domain.PaymentMethod;
import io.spm.parknshop.payment.domain.PaymentType;
import io.spm.parknshop.payment.domain.TransferTransactionRecord;
import io.spm.parknshop.payment.repository.PaymentRecordRepository;
import io.spm.parknshop.payment.repository.TransferTransactionRepository;
import io.spm.parknshop.payment.service.PaymentService;
import io.spm.parknshop.trade.domain.PaymentRedirectData;
import io.spm.parknshop.trade.domain.PaymentResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class PaymentServiceImpl implements PaymentService {

  @Autowired
  private PaymentRecordRepository paymentRecordRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private TransferTransactionRepository transferTransactionRepository;

  @Autowired
  private AlipayService alipayService;
  @Autowired
  private OrderService orderService;

  @Override
  public Mono<PaymentRecord> createPaymentRecord(double totalAmount) {
    if (totalAmount < 0) {
      return Mono.error(ExceptionUtils.invalidParam("fund amount"));
    }
    PaymentRecord record = new PaymentRecord().setStatus(PaymentStatus.NEW_CREATED)
      .setPaymentType(PaymentMethod.ALIPAY).setTotalAmount(totalAmount).setId(generatePaymentId());
    return async(() -> paymentRecordRepository.save(record));
  }

  private String generatePaymentId() {
    // TODO: May conflict! Use business code to avoid conflict or fast-fail!
    return String.format("SP%10d%04d", System.currentTimeMillis(), ThreadLocalRandom.current().nextInt(0, 9999));
  }

  @Override
  public Mono<PaymentRecord> getPaymentById(String id) {
    if (StringUtils.isEmpty(id)) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> paymentRecordRepository.findById(id))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(PAYMENT_NOT_EXIST, "Payment does not exist")));
  }

  @Override
  public Mono<PaymentRedirectData> startPayment(String paymentId, int payMethod, int payType) {
    return getPaymentById(paymentId)
      .flatMap(e -> async(() -> paymentRecordRepository.save(e.setPaymentType(payMethod))))
      .flatMap(payment -> doStartPayment(payment, payType));
  }

  private Mono<PaymentRedirectData> doStartPayment(PaymentRecord record, int payType) {
    if (record.getStatus() != PaymentStatus.NEW_CREATED) {
      return Mono.error(new ServiceException(PAYMENT_CANCELED_OR_FINISHED, "Payment has already been finished or canceled"));
    }
    if (Objects.nonNull(record.getPaymentId())) {
      return Mono.error(new ServiceException(PAYMENT_ALREADY_STARTED, "Payment already in progress"));
    }
    switch (record.getPaymentType()) {
      case PaymentMethod.WECHAT_PAY:
        // TODO: not implemented
      case PaymentMethod.ALIPAY:
        if (payType == PaymentType.BUY_PAY) {
          return alipayService.invokeBuyPayment(record.getId(), "PARKnSHOP.com", record.getTotalAmount())
            .map(e -> new PaymentRedirectData().setPaymentId(record.getId()).setPaymentType(PaymentMethod.ALIPAY).setRenderForm(e));
        } else {
          return alipayService.invokeAdPayment(record.getId(), record.getTotalAmount())
            .map(e -> new PaymentRedirectData().setPaymentId(record.getId()).setPaymentType(PaymentMethod.ALIPAY).setRenderForm(e));
        }
      default:
        return Mono.error(new ServiceException(UNKNOWN_PAYMENT_TYPE, "Unknown payment type"));
    }
  }

  @Override
  public Mono<PaymentRecord> finishPay(String paymentId, String outerPaymentId) {
    return getPaymentById(paymentId)
      .map(e -> e.setPaymentId(outerPaymentId).setStatus(PaymentStatus.PAYED).setGmtModified(new Date()))
      .flatMap(e -> async(() -> paymentRecordRepository.save(e)))
      .flatMap(payment -> doOrderPayed(paymentId)
        .map(e -> payment));
  }

  private Mono<?> doOrderPayed(String paymentId) {
    return asyncIterable(() -> orderRepository.getByPaymentId(paymentId))
      .map(Order::getId)
      .collectList()
      .flatMap(orders -> orderService.finishPay(orders, paymentId));
  }

  @Override
  public Mono<PaymentResult> cancelPay(String proposer, String paymentId) {
    return getPaymentById(paymentId)
      .flatMap(record -> {
        switch (record.getPaymentType()) {
          case PaymentMethod.WECHAT_PAY:
          case PaymentMethod.ALIPAY:
            return alipayService.cancelPayment(paymentId, record.getPaymentId(), proposer)
              .flatMap(e -> cancelStatusInternal(record).map(r -> e));
          default:
            return Mono.error(new ServiceException(UNKNOWN_PAYMENT_TYPE, "Unknown payment type"));
        }
      });
  }

  @Override
  public Mono<TransferTransactionRecord> transferMoney(Long storeId, Long orderId, String alipayAccount, double amount) {
    String transferBusinessNo = String.format("S-TF-P-%10d%08d%08d", System.currentTimeMillis(), storeId, orderId);
    return alipayService.processTransfer(transferBusinessNo, alipayAccount, amount)
      .map(result -> new TransferTransactionRecord().setId(transferBusinessNo).setOrderId(orderId).setStoreId(storeId).setPayeeAccount(alipayAccount).setTransactionNo(result.getTransferTransactionId()).setAmount(amount))
      .flatMap(this::insertNewTransferRecord);
  }

  private Mono<TransferTransactionRecord> insertNewTransferRecord(TransferTransactionRecord record) {
    return async(() -> transferTransactionRepository.save(record));
  }

  private Mono<PaymentRecord> cancelStatusInternal(PaymentRecord record) {
    if (record.getStatus() != PaymentStatus.NEW_CREATED) {
      return Mono.error(new ServiceException(PAYMENT_CANCELED_OR_FINISHED, "Payment has already been finished or canceled"));
    }
    return async(() -> paymentRecordRepository.save(record.setStatus(PaymentStatus.CANCELED).setGmtModified(new Date())));
  }

  @Override
  public Mono<PaymentRefundResult> processRefund(String paymentId, String refundTradeNo, double amount, Long storeId) {
    return getPaymentById(paymentId)
      .flatMap(record -> {
        switch (record.getPaymentType()) {
          case PaymentMethod.WECHAT_PAY:
          case PaymentMethod.ALIPAY:
            return alipayService.processRefund(paymentId, record.getPaymentId(), amount, "Refund of product",
              refundTradeNo, storeId.toString());
          default:
            return Mono.error(new ServiceException(UNKNOWN_PAYMENT_TYPE, "Unknown payment type"));
        }
      });
  }
}
