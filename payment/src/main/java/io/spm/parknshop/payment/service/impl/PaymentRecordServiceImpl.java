package io.spm.parknshop.payment.service.impl;

import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.payment.domain.PaymentStatus;
import io.spm.parknshop.payment.domain.PaymentType;
import io.spm.parknshop.payment.repository.PaymentRecordRepository;
import io.spm.parknshop.payment.service.PaymentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class PaymentRecordServiceImpl implements PaymentRecordService {

  @Autowired
  private PaymentRecordRepository paymentRecordRepository;

  @Override
  public Mono<PaymentRecord> createPaymentRecord() {
    // TODO: check params.
    // TODO: No default type
    PaymentRecord record = new PaymentRecord().setStatus(PaymentStatus.NEW_CREATED)
      .setPaymentType(PaymentType.ALIPAY);
    return async(() -> paymentRecordRepository.save(record));
  }
}
