package io.spm.parknshop.order.repository;

import io.spm.parknshop.order.domain.OrderEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Eric Zhao
 */
public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {

  List<OrderEvent> getByOrderId(long orderId);

}
