package io.spm.parknshop.cart.domain;

import io.spm.parknshop.product.domain.ProductVO;

/**
 * A product unit in cart.
 *
 * @author Eric Zhao
 */
public class CartProduct {

  private Long productId;

  private ProductVO product;
  private Integer amount;

  public Long getProductId() {
    return productId;
  }

  public CartProduct setProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public ProductVO getProduct() {
    return product;
  }

  public CartProduct setProduct(ProductVO product) {
    this.product = product;
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
      "productId=" + productId +
      ", product=" + product +
      ", amount=" + amount +
      '}';
  }
}
