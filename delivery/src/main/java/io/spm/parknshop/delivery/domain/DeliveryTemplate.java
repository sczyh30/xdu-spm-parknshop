package io.spm.parknshop.delivery.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Eric Zhao
 */
@Entity
public class DeliveryTemplate {

  @Id
  @GeneratedValue
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long storeId;

  private int expressType;
  private String description;
  private double defaultPrice;
  private String freightRule;

  public Long getId() {
    return id;
  }

  public DeliveryTemplate setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public DeliveryTemplate setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public DeliveryTemplate setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getStoreId() {
    return storeId;
  }

  public DeliveryTemplate setStoreId(Long storeId) {
    this.storeId = storeId;
    return this;
  }

  public int getExpressType() {
    return expressType;
  }

  public DeliveryTemplate setExpressType(int expressType) {
    this.expressType = expressType;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public DeliveryTemplate setDescription(String description) {
    this.description = description;
    return this;
  }

  public double getDefaultPrice() {
    return defaultPrice;
  }

  public DeliveryTemplate setDefaultPrice(double defaultPrice) {
    this.defaultPrice = defaultPrice;
    return this;
  }

  public String getFreightRule() {
    return freightRule;
  }

  public DeliveryTemplate setFreightRule(String freightRule) {
    this.freightRule = freightRule;
    return this;
  }

  @Override
  public String toString() {
    return "DeliveryTemplate{" +
      "id=" + id +
      ", gmtCreate=" + gmtCreate +
      ", gmtModified=" + gmtModified +
      ", storeId=" + storeId +
      ", expressType=" + expressType +
      ", description='" + description + '\'' +
      ", defaultPrice=" + defaultPrice +
      ", freightRule='" + freightRule + '\'' +
      '}';
  }
}
