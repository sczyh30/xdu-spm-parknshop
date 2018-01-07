package io.spm.parknshop.refund.service;

import io.spm.parknshop.payment.domain.PaymentRefundResult;
import io.spm.parknshop.refund.domain.RefundRecord;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface RefundService {

  Mono<Long> approveRefundRequest(Long id, String responseComment);

  Mono<Long> rejectRefundRequest(Long id, String responseComment);

  Mono<RefundRecord> startRefundProcess(Long refundId);

  Mono<Long> withdrawRefundRequest(Long id);
}
