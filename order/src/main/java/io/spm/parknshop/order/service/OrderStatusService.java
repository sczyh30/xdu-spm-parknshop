package io.spm.parknshop.order.service;

import io.spm.parknshop.order.domain.Order;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface OrderStatusService {

  Mono<Long> prepareShipping(Long orderId);

  Mono<Long> finishShipping(Long orderId);

  Mono<Long> finishDelivery(Long orderId);

  Mono<Long> confirmAndComplete(String proposer, Long orderId);

  Mono<Long> cancelOrder(String proposer, Long orderId);

}
