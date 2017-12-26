package io.spm.parknshop.query.vo;

public class ProductDetailUserVO {

  private Long userId;
  private Long productId;

  private boolean inFavorite;
  private boolean commented;

  public Long getUserId() {
    return userId;
  }

  public ProductDetailUserVO setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getProductId() {
    return productId;
  }

  public ProductDetailUserVO setProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public boolean isInFavorite() {
    return inFavorite;
  }

  public ProductDetailUserVO setInFavorite(boolean inFavorite) {
    this.inFavorite = inFavorite;
    return this;
  }

  public boolean isCommented() {
    return commented;
  }

  public ProductDetailUserVO setCommented(boolean commented) {
    this.commented = commented;
    return this;
  }
}
