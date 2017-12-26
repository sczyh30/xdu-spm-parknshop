package io.spm.parknshop.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Eric Zhao
 */
@Entity
@Table(name = "order_metadata")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long creatorId;

  private Long storeId;
  private Long paymentId;
  private Long addressId;
  private Long deliveryId;

  private Double freightPrice;
  private Double finalTotalPrice;

  private int orderStatus;

  public Long getId() {
    return id;
  }

  public Order setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public Order setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public Order setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getCreatorId() {
    return creatorId;
  }

  public Order setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
    return this;
  }

  public Long getStoreId() {
    return storeId;
  }

  public Order setStoreId(Long storeId) {
    this.storeId = storeId;
    return this;
  }

  public Long getPaymentId() {
    return paymentId;
  }

  public Order setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
    return this;
  }

  public Long getAddressId() {
    return addressId;
  }

  public Order setAddressId(Long addressId) {
    this.addressId = addressId;
    return this;
  }

  public Long getDeliveryId() {
    return deliveryId;
  }

  public Order setDeliveryId(Long deliveryId) {
    this.deliveryId = deliveryId;
    return this;
  }

  public Double getFinalTotalPrice() {
    return finalTotalPrice;
  }

  public Order setFinalTotalPrice(Double finalTotalPrice) {
    this.finalTotalPrice = finalTotalPrice;
    return this;
  }

  public int getOrderStatus() {
    return orderStatus;
  }

  public Order setOrderStatus(int orderStatus) {
    this.orderStatus = orderStatus;
    return this;
  }

  public Double getFreightPrice() {
    return freightPrice;
  }

  public Order setFreightPrice(Double freightPrice) {
    this.freightPrice = freightPrice;
    return this;
  }

  @Override
  public String toString() {
    return "Order{" +
      "id=" + id +
      ", gmtCreate=" + gmtCreate +
      ", gmtModified=" + gmtModified +
      ", creatorId=" + creatorId +
      ", storeId=" + storeId +
      ", paymentId=" + paymentId +
      ", addressId=" + addressId +
      ", deliveryId=" + deliveryId +
      ", freightPrice=" + freightPrice +
      ", finalTotalPrice=" + finalTotalPrice +
      ", orderStatus=" + orderStatus +
      '}';
  }
}
