package io.spm.parknshop.delivery.service;

import io.spm.parknshop.delivery.domain.DeliveryTrackRecord;
import reactor.core.publisher.Mono;

public interface DeliveryRecordService {

  Mono<DeliveryTrackRecord> getById(Long id);

  Mono<DeliveryTrackRecord> getByOrderId(Long orderId);

  Mono<DeliveryTrackRecord> addDeliveryRecord(Long orderId, String outerTrackNo);
}
