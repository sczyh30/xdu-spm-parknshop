package io.spm.parknshop.trade.domain;

import io.spm.parknshop.delivery.domain.DeliveryTemplate;

import java.util.List;

/**
 * Represents a group of products and relevant delivery template in a certain shop.
 *
 * @author Eric Zhao
 */
public class OrderStoreGroupUnit {

  private long storeId;
  private String storeName;

  private List<OrderProductUnit> products;

  private double totalFreight;
  private double totalPrice;
  private int totalAmount;

  private DeliveryTemplate deliveryTemplate;

  public long getStoreId() {
    return storeId;
  }

  public OrderStoreGroupUnit setStoreId(long storeId) {
    this.storeId = storeId;
    return this;
  }

  public String getStoreName() {
    return storeName;
  }

  public OrderStoreGroupUnit setStoreName(String storeName) {
    this.storeName = storeName;
    return this;
  }

  public List<OrderProductUnit> getProducts() {
    return products;
  }

  public OrderStoreGroupUnit setProducts(List<OrderProductUnit> products) {
    this.products = products;
    return this;
  }

  public double getTotalFreight() {
    return totalFreight;
  }

  public OrderStoreGroupUnit setTotalFreight(double totalFreight) {
    this.totalFreight = totalFreight;
    return this;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public OrderStoreGroupUnit setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  public int getTotalAmount() {
    return totalAmount;
  }

  public OrderStoreGroupUnit setTotalAmount(int totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  public DeliveryTemplate getDeliveryTemplate() {
    return deliveryTemplate;
  }

  public OrderStoreGroupUnit setDeliveryTemplate(DeliveryTemplate deliveryTemplate) {
    this.deliveryTemplate = deliveryTemplate;
    return this;
  }
}
