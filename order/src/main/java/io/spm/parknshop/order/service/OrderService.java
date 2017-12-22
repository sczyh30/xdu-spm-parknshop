package io.spm.parknshop.order.service;

import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.trade.domain.OrderStoreGroupUnit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface OrderService {

  Mono<Order> getOrderMetadataById(Long id);

  Flux<OrderEvent> eventStream(Long id);

  Mono<Order> createOrder(Long creatorId, Order rawOrder, OrderStoreGroupUnit storeGroup);

  Mono<Long> modifyOrderStatus(Long orderId, OrderEvent orderEvent);

}
