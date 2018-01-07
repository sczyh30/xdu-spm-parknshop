package io.spm.parknshop.refund.repository;

import io.spm.parknshop.refund.domain.RefundRecord;
import io.spm.parknshop.refund.domain.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface RefundRecordRepository extends JpaRepository<RefundRecord, Long> {

  @Query(value = "SELECT * FROM refund_record WHERE sub_order_id = ?1 ORDER BY id DESC limit 1", nativeQuery = true)
  Optional<RefundRecord> getCurrentRefundBySubOrderId(long subOrderId);

  List<RefundRecord> getByOrderId(long orderId);

  @Query(value = "UPDATE refund_record SET gmt_modified = CURRENT_TIMESTAMP, refund_status = ?1, refund_response_message = ?2 WHERE id = ?3", nativeQuery = true)
  @Modifying
  @Transactional
  void updateWithResponseAndStatus(int status, String responseMessage, long id);
}
