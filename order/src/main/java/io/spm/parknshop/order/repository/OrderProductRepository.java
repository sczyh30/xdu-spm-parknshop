package io.spm.parknshop.order.repository;

import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.domain.SubOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

  @Query(value = "SELECT a.*, b.status AS product_status, b.pic_uri as pic_uri FROM order_product a, product b WHERE a.order_id = ?1 AND b.id = a.product_id", nativeQuery = true)
  List<OrderProduct> getByOrderId(long orderId);

  Optional<OrderProduct> getByOrderIdAndProductId(long orderId, long productId);

  @Query(value = "SELECT a.order_status FROM order_metadata a, order_product b WHERE b.product_id = :productId AND a.id = b.order_id AND a.creator_id = :userId", nativeQuery = true)
  List<Integer> getProductBuyStatusForUser(@Param("userId") long userId, @Param("productId") long productId);

  @Query(value = "SELECT order_id FROM order_product WHERE id = ?1", nativeQuery = true)
  Optional<Long> getOrderId(long subOrderId);

  @Query(value = "SELECT id FROM order_product WHERE id = ?1 AND status = " + SubOrderStatus.ALREADY_REFUND, nativeQuery = true)
  Optional<Long> checkCanRefund(long subOrderId);
}
