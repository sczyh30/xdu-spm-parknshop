package io.spm.parknshop.query.service;

import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.query.vo.OrderVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface OrderQueryService {

  Mono<OrderVO> queryOrderById(Long orderId);

  Flux<OrderVO> queryOrdersByUser(Long userId);

  Flux<OrderVO> queryOrdersByStore(Long userId);

  Flux<OrderEvent> queryOrderEventsById(Long orderId);

}
