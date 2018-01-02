package io.spm.parknshop.delivery.domain;

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
@Table(name = "delivery_record")
public class DeliveryTrackRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Integer deliveryType;
  private String outerDeliveryId;

  private String deliveryData;

  public Long getId() {
    return id;
  }

  public DeliveryTrackRecord setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public DeliveryTrackRecord setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public DeliveryTrackRecord setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Integer getDeliveryType() {
    return deliveryType;
  }

  public DeliveryTrackRecord setDeliveryType(Integer deliveryType) {
    this.deliveryType = deliveryType;
    return this;
  }

  public String getOuterDeliveryId() {
    return outerDeliveryId;
  }

  public DeliveryTrackRecord setOuterDeliveryId(String outerDeliveryId) {
    this.outerDeliveryId = outerDeliveryId;
    return this;
  }

  public String getDeliveryData() {
    return deliveryData;
  }

  public DeliveryTrackRecord setDeliveryData(String deliveryData) {
    this.deliveryData = deliveryData;
    return this;
  }
}
