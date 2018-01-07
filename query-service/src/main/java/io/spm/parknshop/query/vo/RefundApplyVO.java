package io.spm.parknshop.query.vo;

import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;

public class RefundApplyVO {

  private Order order;
  private OrderProduct subOrder;
  private String username;
  private SimpleStoreVO store;

  public RefundApplyVO() {
  }

  public RefundApplyVO(Order order, OrderProduct subOrder, String username, SimpleStoreVO store) {
    this.order = order;
    this.subOrder = subOrder;
    this.username = username;
    this.store = store;
  }

  public Order getOrder() {
    return order;
  }

  public RefundApplyVO setOrder(Order order) {
    this.order = order;
    return this;
  }

  public OrderProduct getSubOrder() {
    return subOrder;
  }

  public RefundApplyVO setSubOrder(OrderProduct subOrder) {
    this.subOrder = subOrder;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public RefundApplyVO setUsername(String username) {
    this.username = username;
    return this;
  }

  public SimpleStoreVO getStore() {
    return store;
  }

  public RefundApplyVO setStore(SimpleStoreVO store) {
    this.store = store;
    return this;
  }
}
