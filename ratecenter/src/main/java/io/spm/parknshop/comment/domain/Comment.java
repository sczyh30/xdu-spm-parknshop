package io.spm.parknshop.comment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Comment entity.
 *
 * @author Eric Zhao
 */
@Entity
public class Comment {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long parentId;
  private Long userId;
  private Long productId;

  private String commentText;
  private Integer rate;

  @Transient
  private String userShowName;
  @Transient
  private int userType;

  public Long getId() {
    return id;
  }

  public Comment setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public Comment setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public Comment setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getParentId() {
    return parentId;
  }

  public Comment setParentId(Long parentId) {
    this.parentId = parentId;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public Comment setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getProductId() {
    return productId;
  }

  public Comment setProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public String getCommentText() {
    return commentText;
  }

  public Comment setCommentText(String commentText) {
    this.commentText = commentText;
    return this;
  }

  public Integer getRate() {
    return rate;
  }

  public Comment setRate(Integer rate) {
    this.rate = rate;
    return this;
  }

  public String getUserShowName() {
    return userShowName;
  }

  public Comment setUserShowName(String userShowName) {
    this.userShowName = userShowName;
    return this;
  }

  public int getUserType() {
    return userType;
  }

  public Comment setUserType(int userType) {
    this.userType = userType;
    return this;
  }

  @Override
  public String toString() {
    return "Comment{" +
      "id=" + id +
      ", gmtCreate=" + gmtCreate +
      ", gmtModified=" + gmtModified +
      ", parentId=" + parentId +
      ", userId=" + userId +
      ", productId=" + productId +
      ", commentText='" + commentText + '\'' +
      ", rate=" + rate +
      ", userShowName='" + userShowName + '\'' +
      ", userType=" + userType +
      '}';
  }
}
