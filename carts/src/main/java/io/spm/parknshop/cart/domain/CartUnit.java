package io.spm.parknshop.cart.domain;

import java.util.List;

/**
 * A cart unit entity for a certain shop.
 *
 * @author Eric Zhao
 */
public class CartUnit {
  private Long storeId;
  private String storeName;

  private List<CartProduct> products;

  private double totalPrice;
  private int totalAmount;

  public Long getStoreId() {
    return storeId;
  }

  public CartUnit setStoreId(Long storeId) {
    this.storeId = storeId;
    return this;
  }

  public String getStoreName() {
    return storeName;
  }

  public CartUnit setStoreName(String storeName) {
    this.storeName = storeName;
    return this;
  }

  public List<CartProduct> getProducts() {
    return products;
  }

  public CartUnit setProducts(List<CartProduct> products) {
    this.products = products;
    return this;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public CartUnit setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  public int getTotalAmount() {
    return totalAmount;
  }

  public CartUnit setTotalAmount(int totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  @Override
  public String toString() {
    return "CartUnit{" +
      "storeId=" + storeId +
      ", storeName='" + storeName + '\'' +
      ", products=" + products +
      ", totalPrice=" + totalPrice +
      ", totalAmount=" + totalAmount +
      '}';
  }
}
