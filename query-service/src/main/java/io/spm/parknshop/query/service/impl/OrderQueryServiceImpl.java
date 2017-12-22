package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.order.repository.OrderEventRepository;
import io.spm.parknshop.query.repository.OrderQueryRepository;
import io.spm.parknshop.query.service.OrderQueryService;
import io.spm.parknshop.query.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * @author Eric Zhao
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {

  @Autowired
  private OrderQueryRepository orderQueryRepository;
  @Autowired
  private OrderEventRepository orderEventRepository;

  @Override
  public Mono<OrderVO> queryOrderById(Long orderId) {
    if (Objects.isNull(orderId) || orderId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("orderId"));
    }
    return async(() -> orderQueryRepository.queryOrder(orderId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.ORDER_NOT_EXIST, "Order does not exist")));
  }

  @Override
  public Flux<OrderVO> queryOrdersByUser(Long userId) {
    if (Objects.isNull(userId) || userId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("userId"));
    }
    return asyncIterable(() -> orderQueryRepository.queryOrderByUser(userId));
  }

  @Override
  public Flux<OrderVO> queryOrdersByStore(Long storeId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> orderQueryRepository.queryOrderByStore(storeId));
  }

  @Override
  public Flux<OrderEvent> queryOrderEventsById(Long orderId) {
    if (Objects.isNull(orderId) || orderId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("orderId"));
    }
    return asyncIterable(() -> orderEventRepository.getByOrderId(orderId))
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.ORDER_NOT_EXIST, "Order does not exist")));
  }
}
