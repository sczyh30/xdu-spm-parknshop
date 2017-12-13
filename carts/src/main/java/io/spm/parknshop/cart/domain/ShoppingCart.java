package io.spm.parknshop.cart.domain;

import java.util.List;

/**
 * @author Eric Zhao
 */
public class ShoppingCart {

  private List<CartProduct> products;
  private double totalPrice;

  public List<CartProduct> getProducts() {
    return products;
  }

  public ShoppingCart setProducts(List<CartProduct> products) {
    this.products = products;
    return this;
  }

  public double getTotalPrice() {
    return totalPrice;
  }

  public ShoppingCart setTotalPrice(double totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  @Override
  public String toString() {
    return "ShoppingCart{" +
      "products=" + products +
      ", totalPrice=" + totalPrice +
      '}';
  }
}
