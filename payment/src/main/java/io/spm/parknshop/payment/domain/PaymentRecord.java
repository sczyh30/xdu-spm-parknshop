package io.spm.parknshop.payment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Eric Zhao
 */
@Entity
public class PaymentRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Integer paymentType;
  private Long paymentId;

  private Integer status;

  public Long getId() {
    return id;
  }

  public PaymentRecord setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public PaymentRecord setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public PaymentRecord setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Integer getPaymentType() {
    return paymentType;
  }

  public PaymentRecord setPaymentType(Integer paymentType) {
    this.paymentType = paymentType;
    return this;
  }

  public Long getPaymentId() {
    return paymentId;
  }

  public PaymentRecord setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
    return this;
  }

  public Integer getStatus() {
    return status;
  }

  public PaymentRecord setStatus(Integer status) {
    this.status = status;
    return this;
  }

  @Override
  public String toString() {
    return "PaymentRecord{" +
      "id=" + id +
      ", gmtCreate=" + gmtCreate +
      ", gmtModified=" + gmtModified +
      ", paymentType=" + paymentType +
      ", paymentId=" + paymentId +
      ", status=" + status +
      '}';
  }
}
