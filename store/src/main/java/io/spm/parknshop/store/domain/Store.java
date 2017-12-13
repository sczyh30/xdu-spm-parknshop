package io.spm.parknshop.store.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Store entity.
 *
 * @author Eric Zhao
 */
@Entity
public class Store {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long sellerId;
  private String name;

  private String briefDescription;
  private String telephone;
  private String email;

  private int status;

  public Long getId() {
    return id;
  }

  public Store setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public Store setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public Store setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public Store setSellerId(Long sellerId) {
    this.sellerId = sellerId;
    return this;
  }

  public String getName() {
    return name;
  }

  public Store setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Store setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getBriefDescription() {
    return briefDescription;
  }

  public Store setBriefDescription(String briefDescription) {
    this.briefDescription = briefDescription;
    return this;
  }

  public String getTelephone() {
    return telephone;
  }

  public Store setTelephone(String telephone) {
    this.telephone = telephone;
    return this;
  }

  public int getStatus() {
    return status;
  }

  public Store setStatus(int status) {
    this.status = status;
    return this;
  }

  @Override
  public String toString() {
    return "Store{" +
      "id=" + id +
      ", gmtCreate=" + gmtCreate +
      ", gmtModified=" + gmtModified +
      ", sellerId=" + sellerId +
      ", name='" + name + '\'' +
      ", briefDescription='" + briefDescription + '\'' +
      ", telephone='" + telephone + '\'' +
      ", status=" + status +
      '}';
  }
}
