package io.spm.parknshop.cart.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eric Zhao
 */
public class ShoppingCart {

  private final List<CartUnit> cart = new ArrayList<>();

  private int totalAmount;
  private double totalPrice;

  public List<CartUnit> getCart() {
    return cart;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public ShoppingCart setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  public int getTotalAmount() {
    return totalAmount;
  }

  public ShoppingCart setTotalAmount(int totalAmount) {
    this.totalAmount = totalAmount;
    return this;
  }

  @Override
  public String toString() {
    return "ShoppingCart{" +
      "cart=" + cart +
      ", totalAmount=" + totalAmount +
      ", totalPrice=" + totalPrice +
      '}';
  }
}
