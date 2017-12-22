package io.spm.parknshop.order.repository;

import io.spm.parknshop.order.domain.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

  List<OrderProduct> getByOrderId(long orderId);

  Optional<OrderProduct> getByOrderIdAndProductId(long orderId, long productId);

}
