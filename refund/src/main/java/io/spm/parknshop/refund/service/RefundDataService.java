package io.spm.parknshop.refund.service;

import io.spm.parknshop.refund.domain.RefundRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RefundDataService {

  Mono<RefundRecord> createRefundRecord(Long subOrderId, String requestComment);

  Mono<RefundRecord> getRefundRecordById(Long refundId);

  Mono<RefundRecord> getCurrentRefundRecordBySubOrderId(Long subOrderId);

  Flux<RefundRecord> getRefundRecordByOrderId(Long orderId);

  Flux<RefundRecord> getRefundRecordByStoreId(Long storeId);

  Flux<RefundRecord> getRefundRecordByUserId(Long userId);
}
