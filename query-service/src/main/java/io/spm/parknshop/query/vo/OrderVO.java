package io.spm.parknshop.query.vo;

import io.spm.parknshop.delivery.domain.DeliveryAddress;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;

import java.util.List;

public class OrderVO {

  private Long id;
  private Order order;
  private SimpleStoreVO store;
  private List<OrderProduct> products;
  private DeliveryAddress deliveryAddress;

  public OrderVO() {
  }

  public OrderVO(Long id, Order order, SimpleStoreVO store, List<OrderProduct> products, DeliveryAddress deliveryAddress) {
    this.id = id;
    this.order = order;
    this.store = store;
    this.products = products;
    this.deliveryAddress = deliveryAddress;
  }

  public Long getId() {
    return id;
  }

  public OrderVO setId(Long id) {
    this.id = id;
    return this;
  }

  public Order getOrder() {
    return order;
  }

  public OrderVO setOrder(Order order) {
    this.order = order;
    return this;
  }

  public SimpleStoreVO getStore() {
    return store;
  }

  public OrderVO setStore(SimpleStoreVO store) {
    this.store = store;
    return this;
  }

  public List<OrderProduct> getProducts() {
    return products;
  }

  public OrderVO setProducts(List<OrderProduct> products) {
    this.products = products;
    return this;
  }

  public DeliveryAddress getDeliveryAddress() {
    return deliveryAddress;
  }

  public OrderVO setDeliveryAddress(DeliveryAddress deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
    return this;
  }
}