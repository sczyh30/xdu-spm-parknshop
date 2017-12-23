package io.spm.parknshop.payment.service;

import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.trade.domain.PaymentRedirectData;
import reactor.core.publisher.Mono;

public interface PaymentService {

  Mono<PaymentRecord> createPaymentRecord(double totalAmount);

  Mono<PaymentRecord> getPaymentById(Long id);

  Mono<PaymentRedirectData> startPayment(Long paymentId);

  Mono<PaymentRecord> finishPay(Long paymentId, String outerPaymentId);

  Mono<PaymentRecord> cancelPay(Long paymentId);
}
