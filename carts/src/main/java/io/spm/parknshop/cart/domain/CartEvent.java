package io.spm.parknshop.cart.domain;

/**
 * @author Eric Zhao
 */
public class CartEvent {

  private Long productId;
  private Integer amount;

  private int eventType;

  public Long getProductId() {
    return productId;
  }

  public CartEvent setProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public Integer getAmount() {
    return amount;
  }

  public CartEvent setAmount(Integer amount) {
    this.amount = amount;
    return this;
  }

  public int getEventType() {
    return eventType;
  }

  public CartEvent setEventType(int eventType) {
    this.eventType = eventType;
    return this;
  }

  @Override
  public String toString() {
    return "CartEvent{" +
        "productId=" + productId +
        ", amount=" + amount +
        ", eventType=" + eventType +
        '}';
  }
}
