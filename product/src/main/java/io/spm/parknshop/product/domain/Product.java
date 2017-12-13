package io.spm.parknshop.product.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author Eric Zhao
 */
@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  private String name;

  private Date gmtCreate;
  private Date gmtModified;

  private Long catalogId;
  private Long storeId;

  private double price;

  private String picUri;
  private String description;

  public Long getId() {
    return id;
  }

  public Product setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Product setName(String name) {
    this.name = name;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public Product setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public Product setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getCatalogId() {
    return catalogId;
  }

  public Product setCatalogId(Long catalogId) {
    this.catalogId = catalogId;
    return this;
  }

  public Long getStoreId() {
    return storeId;
  }

  public Product setStoreId(Long storeId) {
    this.storeId = storeId;
    return this;
  }

  public double getPrice() {
    return price;
  }

  public Product setPrice(double price) {
    this.price = price;
    return this;
  }

  public String getPicUri() {
    return picUri;
  }

  public Product setPicUri(String picUri) {
    this.picUri = picUri;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Product setDescription(String description) {
    this.description = description;
    return this;
  }

  @Override
  public String toString() {
    return "Product{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", gmtCreate=" + gmtCreate +
      ", gmtModified=" + gmtModified +
      ", catalogId=" + catalogId +
      ", storeId=" + storeId +
      ", price=" + price +
      ", picUri='" + picUri + '\'' +
      ", description='" + description + '\'' +
      '}';
  }
}
