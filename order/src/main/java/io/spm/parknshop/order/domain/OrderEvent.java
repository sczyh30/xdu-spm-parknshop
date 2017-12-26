package io.spm.parknshop.order.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Eric Zhao
 */
@Entity
public class OrderEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Date gmtCreate;

  private Long orderId;
  private int orderEventType;

  private String extraData;

  public OrderEvent() {
  }

  public OrderEvent(Long orderId, int orderEventType) {
    this.orderId = orderId;
    this.orderEventType = orderEventType;
  }

  public Long getId() {
    return id;
  }

  public OrderEvent setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public OrderEvent setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Long getOrderId() {
    return orderId;
  }

  public OrderEvent setOrderId(Long orderId) {
    this.orderId = orderId;
    return this;
  }

  public int getOrderEventType() {
    return orderEventType;
  }

  public OrderEvent setOrderEventType(int orderEventType) {
    this.orderEventType = orderEventType;
    return this;
  }

  public String getExtraData() {
    return extraData;
  }

  public OrderEvent setExtraData(String extraData) {
    this.extraData = extraData;
    return this;
  }
}
