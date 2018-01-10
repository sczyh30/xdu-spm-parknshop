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
@Table(name="apply_metadata")
public class Apply {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private String proposerId;

  private int applyType;

  private int status;

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

  public String getProposerId() {
    return proposerId;
  }

  public Apply setProposerId(String proposerId) {
    this.proposerId = proposerId;
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

  public int getStatus() {
    return status;
  }

  public Apply setStatus(int status) {
    this.status = status;
    return this;
  }

  @Override
  public String toString() {
    return "Apply{" +
      "id=" + id +
      ", gmtCreate=" + gmtCreate +
      ", gmtModified=" + gmtModified +
      ", proposerId='" + proposerId + '\'' +
      ", applyType=" + applyType +
      ", status=" + status +
      ", applyData='" + applyData + '\'' +
      '}';
  }
}
