package io.spm.parknshop.order.repository;

import io.spm.parknshop.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Eric Zhao
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query(value = "UPDATE `order_metadata` SET gmt_modified = CURRENT_TIMESTAMP, order_status = ?2 WHERE id = ?1", nativeQuery = true)
  @Transactional
  @Modifying
  void updateStatus(long id, int status);

  int countByStoreId(long storeId);

  List<Order> getByCreatorIdOrderByIdDesc(long creatorId);

  List<Order> getByCreatorIdAndOrderStatus(long creatorId, int status);

  List<Order> getByStoreIdOrderByIdDesc(long storeId);

  @Query(value = "SELECT * FROM order_metadata WHERE store_id = ?1 AND order_status IN (5, 6)", nativeQuery = true)
  List<Order> getFinishedByStoreId(long storeId);

  List<Order> getByStoreIdAndOrderStatus(long storeId, int status);

  List<Order> getByPaymentId(long paymentId);

  @Query(value = "SELECT SUM(final_total_price * commission_snapshot) FROM order_metadata WHERE order_status BETWEEN 1 AND 6", nativeQuery = true)
  double getAllSaleProfit();

  @Query(value = "SELECT SUM(final_total_price) FROM order_metadata WHERE store_id = ?1 AND order_status BETWEEN 1 AND 6", nativeQuery = true)
  double getSaleMoneyForStore(long storeId);

  @Query(value = "SELECT SUM(final_total_price * (100 - commission_snapshot)) FROM order_metadata WHERE store_id = ?1 AND order_status BETWEEN 1 AND 6", nativeQuery = true)
  double getSaleProfitForStore(long storeId);

  @Query(value = "SELECT SUM(final_total_price) FROM order_metadata WHERE store_id = ?1 AND order_status BETWEEN 1 AND 6 AND gmt_create BETWEEN ?2 AND ?3", nativeQuery = true)
  double getSaleMoneyForStoreBetween(long storeId, Date from, Date to);
}
