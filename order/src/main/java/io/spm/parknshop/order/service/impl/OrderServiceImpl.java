package io.spm.parknshop.order.service.impl;

import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.state.StateMachine;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.order.domain.OrderEventType;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.domain.OrderStatus;
import io.spm.parknshop.order.repository.OrderEventRepository;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.order.service.OrderService;
import io.spm.parknshop.trade.domain.OrderProductUnit;
import io.spm.parknshop.trade.domain.OrderStoreGroupUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  @Qualifier("orderStateMachine")
  private StateMachine stateMachine;

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderEventRepository orderEventRepository;
  @Autowired
  private OrderProductRepository orderProductRepository;

  @Override
  public Mono<Order> getOrderMetadataById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> orderRepository.findById(id))
      .filter(Optional::isPresent)
      .switchIfEmpty(Mono.error(new ServiceException(ORDER_NOT_EXIST, "Order does not exist")))
      .map(Optional::get);
  }

  @Override
  public Flux<OrderEvent> eventStream(Long orderId) {
    if (Objects.isNull(orderId) || orderId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("orderId"));
    }
    return asyncIterable(() -> orderEventRepository.getByOrderId(orderId))
      .switchIfEmpty(Mono.error(new ServiceException(ORDER_NOT_EXIST, "Order does not exist")));
  }

  @Override
  public Mono<Order> createOrder(Long creatorId, Order rawOrder, OrderStoreGroupUnit storeGroup) {
    if (Objects.isNull(creatorId) || creatorId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("creatorId"));
    }
    if (Objects.isNull(rawOrder) || Objects.isNull(rawOrder.getPaymentId()) || Objects.nonNull(rawOrder.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("Invalid order"));
    }
    if (Objects.isNull(storeGroup)) {
      return Mono.error(ExceptionUtils.invalidParam("Invalid order products"));
    }
    List<OrderProduct> products = storeGroup.getProducts().stream()
      .map(this::toOrderProduct)
      .collect(Collectors.toList());
    OrderEvent orderEvent = new OrderEvent().setOrderEventType(OrderEventType.NEW_CREATED);
    rawOrder.setFreightPrice(storeGroup.getTotalFreight()).setFinalTotalPrice(storeGroup.getTotalPrice())
      .setOrderStatus(OrderStatus.NEW_CREATED);
    return async(() -> saveOrderInternal(rawOrder, orderEvent, products));
  }

  private OrderProduct toOrderProduct(OrderProductUnit product) {
    return new OrderProduct().setAmount(product.getAmount())
      .setProductId(product.getProductId()).setProductName(product.getProductName())
      .setUnitPrice(product.getPrice()).setTotalPrice(product.getPrice() * product.getAmount());
  }

  @Transactional
  protected Order saveOrderInternal(Order order, OrderEvent orderEvent, List<OrderProduct> products) {
    Date now = new Date();
    Order newOrder = orderRepository.save(order.setGmtCreate(now));
    Long orderId = newOrder.getId();
    orderEventRepository.save(orderEvent.setId(orderId).setGmtCreate(now));
    products.forEach(e ->e.setOrderId(orderId));
    orderProductRepository.saveAll(products);
    return newOrder;
  }

  @Override
  public Mono<Long> modifyOrderStatus(Long orderId, OrderEvent orderEvent) {
    // TODO: add check
    return eventStream(orderId)
      .map(OrderEvent::getOrderEventType)
      .reduce(OrderStatus.NEW_CREATED, stateMachine::transform)
      .flatMap(this::checkNextState)
      .flatMap(nextState -> asyncExecute(() -> modifyOrderStatusInternal(orderEvent, nextState)));
  }

  private Mono<Integer> checkNextState(int state) {
    if (state == OrderStatus.UNEXPECTED_STATE) {
      return Mono.error(new ServiceException(ORDER_UNEXPECTED_STATE, "Unexpected state"));
    } else {
      return Mono.just(state);
    }
  }

  @Transactional
  protected void modifyOrderStatusInternal(OrderEvent orderEvent, int nextState) {
    orderEventRepository.save(orderEvent);
    orderRepository.updateStatus(orderEvent.getOrderId(), nextState);
  }
}
