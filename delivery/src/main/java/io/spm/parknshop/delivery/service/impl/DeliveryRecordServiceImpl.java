package io.spm.parknshop.delivery.service.impl;

import io.spm.parknshop.delivery.domain.DeliveryTrackRecord;
import io.spm.parknshop.delivery.domain.DeliveryType;
import io.spm.parknshop.delivery.repository.DeliveryRecordRepository;
import io.spm.parknshop.delivery.service.DeliveryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class DeliveryRecordServiceImpl implements DeliveryRecordService {

  @Autowired
  private DeliveryRecordRepository deliveryRecordRepository;

  @Override
  public Mono<DeliveryTrackRecord> getById(Long id) {
    return null;
  }

  @Override
  public Mono<DeliveryTrackRecord> getByOrderId(Long orderId) {
    return null;
  }

  @Override
  public Mono<DeliveryTrackRecord> addDeliveryRecord(Long orderId, String outerTrackNo) {
    DeliveryTrackRecord record = new DeliveryTrackRecord().setDeliveryType(DeliveryType.COMMON_EXPRESS)
      .setOuterDeliveryId(outerTrackNo).setOrderId(orderId);
    return async(() -> deliveryRecordRepository.save(record));
  }
}
