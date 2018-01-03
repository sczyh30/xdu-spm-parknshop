package io.spm.parknshop.order.repository;

import io.spm.parknshop.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

  List<OrderProduct> getByOrderId(long orderId);

  Optional<OrderProduct> getByOrderIdAndProductId(long orderId, long productId);

  @Query(value = "SELECT a.order_status FROM order_metadata a, order_product b WHERE b.product_id = :productId AND a.id = b.order_id AND a.creator_id = :userId", nativeQuery = true)
  List<Integer> getProductBuyStatusForUser(@Param("userId") long userId, @Param("productId") long productId);

}
