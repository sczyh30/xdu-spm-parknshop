package io.spm.parknshop.cart.domain;

public class CartProductDO {
  private Long id;
  private Integer amount;

  public Long getId() {
    return id;
  }

  public CartProductDO setId(Long id) {
    this.id = id;
    return this;
  }

  public Integer getAmount() {
    return amount;
  }

  public CartProductDO setAmount(Integer amount) {
    this.amount = amount;
    return this;
  }

  @Override
  public String toString() {
    return "CartProductDO{" +
      "id=" + id +
      ", amount=" + amount +
      '}';
  }
}
