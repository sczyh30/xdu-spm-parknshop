package io.spm.parknshop.trade.domain;

import java.util.List;

/**
 * Order preview for display when confirming order (rendering submit order page).
 *
 * @author Eric Zhao
 */
public class OrderPreview {

  private List<OrderStoreGroupUnit> storeGroups;

  private int totalAmount;

  private double totalFreight;
  private double totalPrice;

  public List<OrderStoreGroupUnit> getStoreGroups() {
    return storeGroups;
  }

  public OrderPreview setStoreGroups(List<OrderStoreGroupUnit> storeGroups) {
    this.storeGroups = storeGroups;
    return this;
  }

  public int getTotalAmount() {
    return totalAmount;
  }

  public OrderPreview setTotalAmount(int totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  public double getTotalFreight() {
    return totalFreight;
  }

  public OrderPreview setTotalFreight(double totalFreight) {
    this.totalFreight = totalFreight;
    return this;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public OrderPreview setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }
}
