package io.spm.parknshop.order.repository;

import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderStatus;
import io.spm.parknshop.order.domain.SubOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

  @Query(value = "SELECT a.* FROM order_metadata a, order_product b WHERE b.product_id = :productId AND a.id = b.order_id AND a.creator_id = :userId", nativeQuery = true)
  List<Order> getOrderForProductAndUser(@Param("userId") long userId, @Param("productId") long productId);

  int countByStoreId(long storeId);

  List<Order> getByCreatorIdOrderByIdDesc(long creatorId);

  List<Order> getByCreatorIdAndOrderStatus(long creatorId, int status);

  List<Order> getByStoreIdOrderByIdDesc(long storeId);

  @Query(value = "SELECT * FROM order_metadata WHERE store_id = ?1 AND order_status IN (5, 6)", nativeQuery = true)
  List<Order> getFinishedByStoreId(long storeId);

  @Query(value = "SELECT * FROM order_metadata WHERE store_id = ?1 AND order_status = " + OrderStatus.COMPLETED + " AND gmt_create BETWEEN ?2 AND DATE_ADD(?3, INTERVAL 1 DAY) ORDER BY id DESC", nativeQuery = true)
  List<Order> getFinishedByStoreIdBetween(long storeId, Date start, Date end);

  List<Order> getByStoreIdAndOrderStatus(long storeId, int status);

  List<Order> getByPaymentId(long paymentId);

  // Sales metrics query.

  @Query(value = "SELECT SUM(final_total_price * commission_snapshot) FROM order_metadata WHERE order_status BETWEEN 1 AND 6", nativeQuery = true)
  Double getAllSaleProfit();

  @Query(value = "SELECT SUM(b.total_price) FROM order_metadata a, order_product b WHERE a.store_id = ?1 AND a.order_status BETWEEN 1 AND 6 AND a.id = b.order_id AND b.status = " + SubOrderStatus.NORMAL, nativeQuery = true)
  Double getSaleMoneyForStore(long storeId);

  @Query(value = "SELECT SUM(b.total_price * (100 - a.commission_snapshot)) FROM order_metadata a, order_product b WHERE a.store_id = ?1 AND a.order_status BETWEEN 1 AND 6 AND a.id = b.order_id AND b.status = " + SubOrderStatus.NORMAL, nativeQuery = true)
  Double getSaleProfitForStore(long storeId);

  @Query(value = "SELECT SUM(b.total_price) FROM order_metadata a, order_product b WHERE a.store_id = ?1 AND a.order_status = " + OrderStatus.COMPLETED + " AND a.id = b.order_id AND b.status = " + SubOrderStatus.NORMAL, nativeQuery = true)
  Double getTotalRawSaleIncomeForStore(long storeId);

  @Query(value = "SELECT SUM(b.total_price * (100 - a.commission_snapshot) / 100) FROM order_metadata a, order_product b WHERE a.store_id = ?1 AND a.order_status = " + OrderStatus.COMPLETED + " AND a.id = b.order_id AND b.status = " + SubOrderStatus.NORMAL, nativeQuery = true)
  Double getTotalProfitForStore(long storeId);

  @Query(value = "SELECT SUM(b.total_price) FROM order_metadata a, order_product b WHERE a.store_id = ?1 AND a.order_status = " + OrderStatus.COMPLETED + " AND a.id = b.order_id AND b.status = " + SubOrderStatus.NORMAL + " AND gmt_create BETWEEN ?2 AND DATE_ADD(?3, INTERVAL 1 DAY)", nativeQuery = true)
  Double getRawSaleIncomeForStoreBetween(long storeId, Date from, Date to);

  @Query(value = "SELECT SUM(b.total_price) FROM order_metadata a, order_product b WHERE a.store_id = ?1 AND a.order_status = " + OrderStatus.COMPLETED + " AND a.id = b.order_id AND b.status = " + SubOrderStatus.NORMAL + " AND gmt_create BETWEEN ?2 AND DATE_ADD(?2, INTERVAL 1 DAY)", nativeQuery = true)
  Double getRawSaleIncomeForStoreAtDate(long storeId, Date date);

  @Query(value = "SELECT SUM(b.total_price * (100 - a.commission_snapshot) / 100) FROM order_metadata a, order_product b WHERE a.store_id = ?1 AND a.order_status = " + OrderStatus.COMPLETED + " AND a.id = b.order_id AND b.status = " + SubOrderStatus.NORMAL + " AND gmt_create BETWEEN ?2 AND DATE_ADD(?3, INTERVAL 1 DAY)", nativeQuery = true)
  Double getProfitForStoreBetween(long storeId, Date from, Date to);

  @Query(value = "SELECT SUM(b.total_price * (100 - a.commission_snapshot) / 100) FROM order_metadata a, order_product b WHERE a.store_id = ?1 AND a.order_status = " + OrderStatus.COMPLETED + " AND a.id = b.order_id AND b.status = " + SubOrderStatus.NORMAL + " AND gmt_create BETWEEN ?2 AND DATE_ADD(?2, INTERVAL 1 DAY)", nativeQuery = true)
  Double getProfitForStoreAtDate(long storeId, Date date);
}
