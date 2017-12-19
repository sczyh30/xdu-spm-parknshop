package io.spm.parknshop.apply.domain;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public class ApplyEvent {
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long applyId;
  private int applyEventType;

  private String processorId;

  private String extraInfo;

  public Long getId() {
    return id;
  }

  public ApplyEvent setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public ApplyEvent setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public ApplyEvent setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getApplyId() {
    return applyId;
  }

  public ApplyEvent setApplyId(Long applyId) {
    this.applyId = applyId;
    return this;
  }

  public int getApplyEventType() {
    return applyEventType;
  }

  public ApplyEvent setApplyEventType(int applyEventType) {
    this.applyEventType = applyEventType;
    return this;
  }

  public String getProcessorId() {
    return processorId;
  }

  public ApplyEvent setProcessorId(String processorId) {
    this.processorId = processorId;
    return this;
  }

  public String getExtraInfo() {
    return extraInfo;
  }

  public ApplyEvent setExtraInfo(String extraInfo) {
    this.extraInfo = extraInfo;
    return this;
  }
}
