package io.spm.parknshop.query.vo;

import io.spm.parknshop.delivery.domain.DeliveryAddress;
import io.spm.parknshop.delivery.domain.DeliveryTrackRecord;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.user.domain.User;

import java.util.List;

public class OrderVO {

  private Long id;
  private Order order;
  private SimpleStoreVO store;
  private List<OrderProduct> products;
  private PaymentRecord payment;
  private DeliveryAddress deliveryAddress;
  private User user;
  private DeliveryTrackRecord delivery;

  private double refundFee;

  public OrderVO() {
  }

  public OrderVO(Long id, Order order, SimpleStoreVO store, List<OrderProduct> products, PaymentRecord payment, DeliveryAddress deliveryAddress, User user) {
    this.id = id;
    this.order = order;
    this.store = store;
    this.products = products;
    this.payment = payment;
    this.deliveryAddress = deliveryAddress;
    this.user = user;
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

  public PaymentRecord getPayment() {
    return payment;
  }

  public OrderVO setPayment(PaymentRecord payment) {
    this.payment = payment;
    return this;
  }

  public User getUser() {
    return user;
  }

  public OrderVO setUser(User user) {
    this.user = user;
    return this;
  }

  public double getRefundFee() {
    return refundFee;
  }

  public OrderVO setRefundFee(double refundFee) {
    this.refundFee = refundFee;
    return this;
  }

  public DeliveryTrackRecord getDelivery() {
    return delivery;
  }

  public OrderVO setDelivery(DeliveryTrackRecord delivery) {
    this.delivery = delivery;
    return this;
  }
}
