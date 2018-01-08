package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.DateUtils;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.order.repository.OrderEventRepository;
import io.spm.parknshop.query.repository.OrderQueryRepository;
import io.spm.parknshop.query.service.OrderQueryService;
import io.spm.parknshop.query.vo.OrderVO;
import io.spm.parknshop.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;
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

  @Autowired
  private StoreService storeService;

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
  public Flux<OrderVO> queryOrdersByUserBetween(Long userId, Date start, Date end) {
    if (Objects.isNull(userId) || userId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("userId"));
    }
    return asyncIterable(() -> orderQueryRepository.queryOrderByUserBetween(userId, start, end));
  }

  @Override
  public Flux<OrderVO> queryOrdersByUserDaily(Long userId) {
    return queryOrdersByUserBetween(userId, DateUtils.toDate(LocalDate.now()), DateUtils.toDate(LocalDate.now()));
  }

  @Override
  public Flux<OrderVO> queryOrdersByUserWeekly(Long userId) {
    return queryOrdersByUserBetween(userId, DateUtils.toDate(LocalDate.now().minusDays(7)), DateUtils.toDate(LocalDate.now()));
  }

  @Override
  public Flux<OrderVO> queryOrdersByUserMonthly(Long userId) {
    return queryOrdersByUserBetween(userId, DateUtils.toDate(LocalDate.now().minusMonths(1)), DateUtils.toDate(LocalDate.now()));
  }

  @Override
  public Flux<OrderVO> queryOrdersByUserYearly(Long userId) {
    return queryOrdersByUserBetween(userId, DateUtils.toDate(LocalDate.now().minusYears(1)), DateUtils.toDate(LocalDate.now()));
  }

  @Override
  public Flux<OrderVO> queryFinishedOrdersBySeller(Long sellerId) {
    if (Objects.isNull(sellerId) || sellerId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("sellerId"));
    }
    return storeService.getBySellerId(sellerId)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .flatMapMany(e -> queryFinishedOrdersByStore(e.getId()));
  }

  @Override
  public Flux<OrderVO> queryOrdersBySeller(Long sellerId) {
    if (Objects.isNull(sellerId) || sellerId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("sellerId"));
    }
    return storeService.getBySellerId(sellerId)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .flatMapMany(e -> queryOrdersByStore(e.getId()));
  }

  @Override
  public Flux<OrderVO> queryOrdersByStore(Long storeId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> orderQueryRepository.queryOrderByStore(storeId));
  }

  @Override
  public Flux<OrderVO> queryFinishedOrdersByStore(Long storeId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> orderQueryRepository.queryFinishedOrderByStore(storeId));
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
