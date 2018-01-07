package io.spm.parknshop.payment.service;

import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.payment.domain.PaymentRefundResult;
import io.spm.parknshop.payment.domain.TransferTransactionRecord;
import io.spm.parknshop.trade.domain.PaymentRedirectData;
import io.spm.parknshop.trade.domain.PaymentResult;
import reactor.core.publisher.Mono;

public interface PaymentService {

  Mono<PaymentRecord> createPaymentRecord(double totalAmount);

  Mono<PaymentRecord> getPaymentById(Long id);

  Mono<PaymentRedirectData> startPayment(Long paymentId, int payMethod, int payType);

  Mono<PaymentRecord> finishPay(Long paymentId, String outerPaymentId);

  Mono<PaymentResult> cancelPay(String proposer, Long paymentId);

  Mono<TransferTransactionRecord> transferMoney(Long storeId, Long orderId, String alipayAccount, double amount);

  Mono<PaymentRefundResult> processRefund(String paymentId, String refundTradeNo, double amount, Long storeId);
}
