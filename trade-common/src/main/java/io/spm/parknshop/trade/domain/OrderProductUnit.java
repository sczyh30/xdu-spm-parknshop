package io.spm.parknshop.trade.domain;

/**
 * @author Eric Zhao
 */
public class OrderProductUnit {

  private Long productId;
  private String productName;

  private String picUri;

  private Integer amount;
  private Integer inventory;

  private double price;

  public Long getProductId() {
    return productId;
  }

  public OrderProductUnit setProductId(Long productId) {
    this.productId = productId;
    return this;
  }

  public String getProductName() {
    return productName;
  }

  public OrderProductUnit setProductName(String productName) {
    this.productName = productName;
    return this;
  }

  public String getPicUri() {
    return picUri;
  }

  public OrderProductUnit setPicUri(String picUri) {
    this.picUri = picUri;
    return this;
  }

  public Integer getAmount() {
    return amount;
  }

  public OrderProductUnit setAmount(Integer amount) {
    this.amount = amount;
    return this;
  }

  public Integer getInventory() {
    return inventory;
  }

  public OrderProductUnit setInventory(Integer inventory) {
    this.inventory = inventory;
    return this;
  }

  public double getPrice() {
    return price;
  }

  public OrderProductUnit setPrice(double price) {
    this.price = price;
    return this;
  }
}
