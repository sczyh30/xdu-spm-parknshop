package io.spm.parknshop.order.service.impl;

import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderEvent;
import io.spm.parknshop.order.service.OrderService;
import io.spm.parknshop.trade.domain.OrderStoreGroupUnit;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OrderServiceImpl implements OrderService {
  @Override
  public Mono<Order> getOrderMetadataById(Long id) {
    return null;
  }

  @Override
  public Mono<Order> createOrder(Long creator, Long paymentId, OrderStoreGroupUnit storeGroup) {
    return null;
  }

  @Override
  public Mono<Long> modifyOrderStatus(Long orderId, OrderEvent orderEvent) {
    return null;
  }
}
