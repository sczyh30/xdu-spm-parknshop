package io.spm.parknshop.delivery.repository;

import io.spm.parknshop.delivery.domain.DeliveryTrackRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Eric Zhao
 */
public interface DeliveryRecordRepository extends JpaRepository<DeliveryTrackRecord, Long> {
}
