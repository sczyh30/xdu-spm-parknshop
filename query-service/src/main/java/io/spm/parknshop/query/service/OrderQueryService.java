package io.spm.parknshop.query.service;

import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.query.vo.OrderVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public interface OrderQueryService {

  Mono<OrderVO> queryOrderById(Long orderId);

  Flux<OrderVO> queryOrdersByUser(Long userId);

  Flux<OrderVO> queryOrdersBySeller(Long sellerId);

  Flux<OrderVO> queryFinishedOrdersBySeller(Long sellerId);

  Flux<OrderVO> queryOrdersByStore(Long userId);

  Flux<OrderVO> queryFinishedOrdersByStore(Long storeId);

  Flux<OrderEvent> queryOrderEventsById(Long orderId);

  Flux<OrderVO> queryOrdersByUserBetween(Long userId, Date start, Date end);

  Flux<OrderVO> queryOrdersByUserDaily(Long userId);

  Flux<OrderVO> queryOrdersByUserWeekly(Long userId);

  Flux<OrderVO> queryOrdersByUserMonthly(Long userId);

  Flux<OrderVO> queryOrdersByUserYearly(Long userId);
}
