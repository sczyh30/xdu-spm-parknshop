package io.spm.parknshop.order.repository;

import io.spm.parknshop.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Eric Zhao
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> getByCreatorId(long creatorId);

  List<Order> getByCreatorIdAndOrderStatus(long creatorId, int status);

  List<Order> getByStoreId(long storeId);

  List<Order> getByStoreIdAndOrderStatus(long storeId, int status);

  List<Order> getByPaymentId(long paymentId);

}
