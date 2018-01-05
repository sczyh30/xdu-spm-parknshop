package io.spm.parknshop.payment.service;

import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.trade.domain.PaymentRedirectData;
import io.spm.parknshop.trade.domain.PaymentResult;
import reactor.core.publisher.Mono;

public interface PaymentService {

  Mono<PaymentRecord> createPaymentRecord(double totalAmount);

  Mono<PaymentRecord> getPaymentById(Long id);

  Mono<PaymentRedirectData> startPayment(Long paymentId);

  Mono<PaymentRecord> finishPay(Long paymentId, String outerPaymentId);

  Mono<PaymentResult> cancelPay(String proposer, Long paymentId);
}
