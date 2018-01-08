package io.spm.parknshop.query.vo;

public class ProductDetailUserVO {

  private Long userId;
  private Long productId;

  private boolean inFavorite;
  private boolean canComment;

  public ProductDetailUserVO() {
  }

  public ProductDetailUserVO(Long userId, Long productId, boolean inFavorite, boolean canComment) {
    this.userId = userId;
    this.productId = productId;
    this.inFavorite = inFavorite;
    this.canComment = canComment;
  }

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

  public boolean isCanComment() {
    return canComment;
  }

  public ProductDetailUserVO setCanComment(boolean canComment) {
    this.canComment = canComment;
    return this;
  }
}
