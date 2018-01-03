package io.spm.parknshop.comment.domain;

/**
 * Comment create DTO.
 *
 * @author Eric Zhao
 */
public class CommentDTO {

  private String type;

  private Long parentId;
  private Long productId;

  private String commentText;
  private Integer rate;

  public String getType() {
    return type;
  }

  public CommentDTO setType(String type) {
    this.type = type;
    return this;
  }

  public Long getParentId() {
    return parentId;
  }

  public CommentDTO setParentId(Long parentId) {
    this.parentId = parentId;
    return this;
  }

  public Long getProductId() {
    return productId;
  }

  public CommentDTO setProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public String getCommentText() {
    return commentText;
  }

  public CommentDTO setCommentText(String commentText) {
    this.commentText = commentText;
    return this;
  }

  public Integer getRate() {
    return rate;
  }

  public CommentDTO setRate(Integer rate) {
    this.rate = rate;
    return this;
  }
}
