package io.spm.parknshop.delivery.repository;

import io.spm.parknshop.delivery.domain.DeliveryTrackRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface DeliveryRecordRepository extends JpaRepository<DeliveryTrackRecord, Long> {

  Optional<DeliveryTrackRecord> getByOrderId(long orderId);
}
