package io.spm.parknshop.query.vo;

import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.refund.domain.RefundRecord;

public class RefundVO {

  private RefundRecord refundRecord;

  private Order order;
  private OrderProduct subOrder;
  private SimpleStoreVO store;
  private String username;

  public RefundVO() {
  }

  public RefundVO(RefundRecord refundRecord, Order order, OrderProduct subOrder, SimpleStoreVO store, String username) {
    this.refundRecord = refundRecord;
    this.order = order;
    this.subOrder = subOrder;
    this.store = store;
    this.username = username;
  }

  public RefundRecord getRefundRecord() {
    return refundRecord;
  }

  public RefundVO setRefundRecord(RefundRecord refundRecord) {
    this.refundRecord = refundRecord;
    return this;
  }

  public Order getOrder() {
    return order;
  }

  public RefundVO setOrder(Order order) {
    this.order = order;
    return this;
  }

  public OrderProduct getSubOrder() {
    return subOrder;
  }

  public RefundVO setSubOrder(OrderProduct subOrder) {
    this.subOrder = subOrder;
    return this;
  }

  public SimpleStoreVO getStore() {
    return store;
  }

  public RefundVO setStore(SimpleStoreVO store) {
    this.store = store;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public RefundVO setUsername(String username) {
    this.username = username;
    return this;
  }
}
