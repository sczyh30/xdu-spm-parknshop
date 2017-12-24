package io.spm.parknshop.order.repository;

import io.spm.parknshop.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Eric Zhao
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query(value = "UPDATE `order_metadata` SET gmt_modified = CURRENT_TIMESTAMP, order_status = ?2 WHERE id = ?1", nativeQuery = true)
  @Transactional
  @Modifying
  void updateStatus(long id, int status);

  List<Order> getByCreatorIdOrderByIdDesc(long creatorId);

  List<Order> getByCreatorIdAndOrderStatus(long creatorId, int status);

  List<Order> getByStoreId(long storeId);

  List<Order> getByStoreIdAndOrderStatus(long storeId, int status);

  List<Order> getByPaymentId(long paymentId);

}
