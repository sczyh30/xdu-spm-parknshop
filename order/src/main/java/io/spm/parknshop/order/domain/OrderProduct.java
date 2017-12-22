package io.spm.parknshop.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Eric Zhao
 */
@Entity
public class OrderProduct {

  @Id
  @GeneratedValue
  private Long id;

  private Long orderId;

  private Long productId;
  private String productName;
  private Integer amount;

  private Double unitPrice;
  private Double totalPrice;

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
}
