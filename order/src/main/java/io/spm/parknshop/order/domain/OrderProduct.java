package io.spm.parknshop.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author Eric Zhao
 */
@Entity
public class OrderProduct {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long orderId;

  private Long productId;
  private String productName;
  private Integer amount;

  private Double unitPrice;
  private Double totalPrice;

  private int status;

  @Transient
  private Integer productStatus;
  @Transient
  private String picUri;
  @Transient
  private Long refundId;

  public Long getId() {
    return id;
  }

  public OrderProduct setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getOrderId() {
    return orderId;
  }

  public OrderProduct setOrderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  public Long getProductId() {
    return productId;
  }

  public OrderProduct setProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public Integer getAmount() {
    return amount;
  }

  public OrderProduct setAmount(Integer amount) {
    this.amount = amount;
    return this;
  }

  public Double getUnitPrice() {
    return unitPrice;
  }

  public OrderProduct setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public Double getTotalPrice() {
    return totalPrice;
  }

  public OrderProduct setTotalPrice(Double totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  public String getProductName() {
    return productName;
  }

  public OrderProduct setProductName(String productName) {
    this.productName = productName;
    return this;
  }

  public int getStatus() {
    return status;
  }

  public OrderProduct setStatus(int status) {
    this.status = status;
    return this;
  }

  public Integer getProductStatus() {
    return productStatus;
  }

  public OrderProduct setProductStatus(Integer productStatus) {
    this.productStatus = productStatus;
    return this;
  }

  public String getPicUri() {
    return picUri;
  }

  public OrderProduct setPicUri(String picUri) {
    this.picUri = picUri;
    return this;
  }

  public Long getRefundId() {
    return refundId;
  }

  public OrderProduct setRefundId(Long refundId) {
    this.refundId = refundId;
    return this;
  }
}
