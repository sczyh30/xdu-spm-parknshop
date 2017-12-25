package io.spm.parknshop.order.service.impl;

import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.order.domain.OrderEventType;
import io.spm.parknshop.order.service.OrderService;
import io.spm.parknshop.order.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class OrderStatusServiceImpl implements OrderStatusService {

  @Autowired
  private OrderService orderService;

  @Override
  public Mono<Long> prepareShipping(Long orderId) {
    // TODO: check
    return orderService.modifyOrderStatus(orderId, new OrderEvent(orderId, OrderEventType.PROCESS_ORDER_SHIPMENT));
  }

  @Override
  public Mono<Long> finishShipping(Long orderId) {
    return orderService.modifyOrderStatus(orderId, new OrderEvent(orderId, OrderEventType.FINISH_SHIPMENT));
  }

  @Override
  public Mono<Long> finishDelivery(Long orderId) {
    return orderService.modifyOrderStatus(orderId, new OrderEvent(orderId, OrderEventType.FINISH_DELIVERY));
  }

  @Override
  public Mono<Long> confirmAndComplete(String proposer, Long orderId) {
    // TODO: the proposer should also be recorded in DB
    return orderService.modifyOrderStatus(orderId, new OrderEvent(orderId, OrderEventType.CONFIRM_ORDER));
  }

  @Override
  public Mono<Long> cancelOrder(String proposer, Long orderId) {
    // TODO: the proposer should also be recorded in DB
    return orderService.modifyOrderStatus(orderId, new OrderEvent(orderId, OrderEventType.CANCEL_ORDER));
  }
}
