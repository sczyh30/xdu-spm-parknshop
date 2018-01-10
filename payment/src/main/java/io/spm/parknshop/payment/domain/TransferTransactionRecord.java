package io.spm.parknshop.payment.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "transfer_transaction")
public class TransferTransactionRecord {

  @Id
  private String id;

  private Date gmtCreate;

  private Long storeId;
  private Long orderId;

  private String transactionNo;
  private String payeeAccount;
  private double amount;

  public String getId() {
    return id;
  }

  public TransferTransactionRecord setId(String id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public TransferTransactionRecord setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Long getStoreId() {
    return storeId;
  }

  public TransferTransactionRecord setStoreId(Long storeId) {
    this.storeId = storeId;
    return this;
  }

  public Long getOrderId() {
    return orderId;
  }

  public TransferTransactionRecord setOrderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  public String getTransactionNo() {
    return transactionNo;
  }

  public TransferTransactionRecord setTransactionNo(String transactionNo) {
    this.transactionNo = transactionNo;
    return this;
  }

  public String getPayeeAccount() {
    return payeeAccount;
  }

  public TransferTransactionRecord setPayeeAccount(String payeeAccount) {
    this.payeeAccount = payeeAccount;
    return this;
  }

  public double getAmount() {
    return amount;
  }

  public TransferTransactionRecord setAmount(double amount) {
    this.amount = amount;
    return this;
  }

  @Override
  public String toString() {
    return "TransferTransactionRecord{" +
      "id='" + id + '\'' +
      ", gmtCreate=" + gmtCreate +
      ", storeId=" + storeId +
      ", orderId=" + orderId +
      ", transactionNo='" + transactionNo + '\'' +
      ", payeeAccount='" + payeeAccount + '\'' +
      ", amount=" + amount +
      '}';
  }
}
