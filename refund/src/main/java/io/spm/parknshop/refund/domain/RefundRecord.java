package io.spm.parknshop.refund.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Eric Zhao
 */
@Entity
public class RefundRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long orderId;
  private Long subOrderId;
  private Long storeId;

  private String refundTransactionNo;
  private double refundAmount;
  private Date refundTime;

  private String buyPaymentId;
  private Long customerId;

  private int refundStatus;

  private String refundRequestMessage;
  private String refundResponseMessage;

  public Long getId() {
    return id;
  }

  public RefundRecord setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getStoreId() {
    return storeId;
  }

  public RefundRecord setStoreId(Long storeId) {
    this.storeId = storeId;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public RefundRecord setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public RefundRecord setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getOrderId() {
    return orderId;
  }

  public RefundRecord setOrderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  public Long getSubOrderId() {
    return subOrderId;
  }

  public RefundRecord setSubOrderId(Long subOrderId) {
    this.subOrderId = subOrderId;
    return this;
  }

  public String getRefundTransactionNo() {
    return refundTransactionNo;
  }

  public RefundRecord setRefundTransactionNo(String refundTransactionNo) {
    this.refundTransactionNo = refundTransactionNo;
    return this;
  }

  public double getRefundAmount() {
    return refundAmount;
  }

  public RefundRecord setRefundAmount(double refundAmount) {
    this.refundAmount = refundAmount;
    return this;
  }

  public Date getRefundTime() {
    return refundTime;
  }

  public RefundRecord setRefundTime(Date refundTime) {
    this.refundTime = refundTime;
    return this;
  }

  public String getBuyPaymentId() {
    return buyPaymentId;
  }

  public RefundRecord setBuyPaymentId(String buyPaymentId) {
    this.buyPaymentId = buyPaymentId;
    return this;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public RefundRecord setCustomerId(Long customerId) {
    this.customerId = customerId;
    return this;
  }

  public int getRefundStatus() {
    return refundStatus;
  }

  public RefundRecord setRefundStatus(int refundStatus) {
    this.refundStatus = refundStatus;
    return this;
  }

  public String getRefundRequestMessage() {
    return refundRequestMessage;
  }

  public RefundRecord setRefundRequestMessage(String refundRequestMessage) {
    this.refundRequestMessage = refundRequestMessage;
    return this;
  }

  public String getRefundResponseMessage() {
    return refundResponseMessage;
  }

  public RefundRecord setRefundResponseMessage(String refundResponseMessage) {
    this.refundResponseMessage = refundResponseMessage;
    return this;
  }
}
