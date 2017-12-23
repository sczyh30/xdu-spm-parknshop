package io.spm.parknshop.payment.service;

import io.spm.parknshop.payment.domain.PaymentRecord;
import reactor.core.publisher.Mono;

public interface PaymentRecordService {

  Mono<PaymentRecord> createPaymentRecord();

}
