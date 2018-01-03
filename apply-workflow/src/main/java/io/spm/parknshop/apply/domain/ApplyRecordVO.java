package io.spm.parknshop.apply.domain;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public class ApplyRecordVO {

  private Date processTime;

  private int applyEventType;

  private Long processorId;
  private int processorRole;
  private String processorName;

  private String extraInfo;

  public Date getProcessTime() {
    return processTime;
  }

  public ApplyRecordVO setProcessTime(Date processTime) {
    this.processTime = processTime;
    return this;
  }

  public int getApplyEventType() {
    return applyEventType;
  }

  public ApplyRecordVO setApplyEventType(int applyEventType) {
    this.applyEventType = applyEventType;
    return this;
  }

  public Long getProcessorId() {
    return processorId;
  }

  public ApplyRecordVO setProcessorId(Long processorId) {
    this.processorId = processorId;
    return this;
  }

  public int getProcessorRole() {
    return processorRole;
  }

  public ApplyRecordVO setProcessorRole(int processorRole) {
    this.processorRole = processorRole;
    return this;
  }

  public String getProcessorName() {
    return processorName;
  }

  public ApplyRecordVO setProcessorName(String processorName) {
    this.processorName = processorName;
    return this;
  }

  public String getExtraInfo() {
    return extraInfo;
  }

  public ApplyRecordVO setExtraInfo(String extraInfo) {
    this.extraInfo = extraInfo;
    return this;
  }
}
