package io.spm.parknshop.apply.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Eric Zhao
 */
@Entity
@Table(name="apply_event")
public class ApplyEvent {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Date gmtCreate;

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
