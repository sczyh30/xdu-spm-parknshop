package io.spm.parknshop.cart.domain;

/**
 * @author Eric Zhao
 */
public class CartProduct {

  private Long id;

  private Long sellerId;

  private Double price;
  private Integer amount;

  public Long getId() {
    return id;
  }

  public CartProduct setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public CartProduct setSellerId(Long sellerId) {
    this.sellerId = sellerId;
    return this;
  }

  public Double getPrice() {
    return price;
  }

  public CartProduct setPrice(Double price) {
    this.price = price;
    return this;
  }

  public Integer getAmount() {
    return amount;
  }

  public CartProduct setAmount(Integer amount) {
    this.amount = amount;
    return this;
  }

  @Override
  public String toString() {
    return "CartProduct{" +
      "id=" + id +
      ", sellerId=" + sellerId +
      ", price=" + price +
      ", amount=" + amount +
      '}';
  }
}
