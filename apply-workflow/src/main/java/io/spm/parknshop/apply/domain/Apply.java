package io.spm.parknshop.apply.domain;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public class Apply {

  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long userId;

  private int applyType;

  private String applyData;

  public Long getId() {
    return id;
  }

  public Apply setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public Apply setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public Apply setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public Apply setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public int getApplyType() {
    return applyType;
  }

  public Apply setApplyType(int applyType) {
    this.applyType = applyType;
    return this;
  }

  public String getApplyData() {
    return applyData;
  }

  public Apply setApplyData(String applyData) {
    this.applyData = applyData;
    return this;
  }
}
