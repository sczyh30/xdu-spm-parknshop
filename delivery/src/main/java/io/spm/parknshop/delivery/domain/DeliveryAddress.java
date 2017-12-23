package io.spm.parknshop.delivery.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author Eric Zhao
 */
@Entity
public class DeliveryAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long userId;

  private String name;
  private String telephone;
  private String detailAddress;

  public Long getId() {
    return id;
  }

  public DeliveryAddress setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public DeliveryAddress setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public DeliveryAddress setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public DeliveryAddress setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public String getName() {
    return name;
  }

  public DeliveryAddress setName(String name) {
    this.name = name;
    return this;
  }

  public String getTelephone() {
    return telephone;
  }

  public DeliveryAddress setTelephone(String telephone) {
    this.telephone = telephone;
    return this;
  }

  public String getDetailAddress() {
    return detailAddress;
  }

  public DeliveryAddress setDetailAddress(String detailAddress) {
    this.detailAddress = detailAddress;
    return this;
  }

  @Override
  public String toString() {
    return "DeliveryAddress{" +
      "id=" + id +
      ", gmtCreate=" + gmtCreate +
      ", gmtModified=" + gmtModified +
      ", userId=" + userId +
      ", name='" + name + '\'' +
      ", telephone='" + telephone + '\'' +
      ", detailAddress='" + detailAddress + '\'' +
      '}';
  }
}
