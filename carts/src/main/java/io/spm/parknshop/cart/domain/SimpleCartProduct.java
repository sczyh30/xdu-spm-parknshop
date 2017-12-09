package io.spm.parknshop.cart.domain;

public class SimpleCartProduct {

  private Long id;
  private Integer amount;

  public SimpleCartProduct() {}

  public SimpleCartProduct(Long id, Integer amount) {
    this.id = id;
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public SimpleCartProduct setId(Long id) {
    this.id = id;
    return this;
  }

  public Integer getAmount() {
    return amount;
  }

  public SimpleCartProduct setAmount(Integer amount) {
    this.amount = amount;
    return this;
  }

  public SimpleCartProduct plusAmount(int n) {
    if (n <= 0) {
      throw new IllegalStateException("Negative n");
    }
    return this.setAmount(amount + n);
  }

  public SimpleCartProduct decreaseAmount(int n) {
    if (n <= 0) {
      throw new IllegalStateException("Positive amount decrease is not allowed");
    }
    int target = amount - n > 0 ? amount - n : 0;
    return this.setAmount(target);
  }

  @Override
  public String toString() {
    return "SimpleCartProduct{" +
      "id=" + id +
      ", amount=" + amount +
      '}';
  }
}
