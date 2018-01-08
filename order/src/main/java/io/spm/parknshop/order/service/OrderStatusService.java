package io.spm.parknshop.order.service;

import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface OrderStatusService {

  Mono<Long> prepareShipping(Long orderId);

  Mono<Long> finishShipping(Long orderId, String trackNo);

  Mono<Long> finishDelivery(Long orderId);

  Mono<Long> confirmAndComplete(String proposer, Long orderId);

  Mono<Long> cancelOrder(String proposer, Long orderId);

  Mono<Integer> getProductBuyStatusForUser(Long userId, Long productId);
}
