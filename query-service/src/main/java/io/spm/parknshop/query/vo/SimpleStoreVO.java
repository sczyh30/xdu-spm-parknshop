package io.spm.parknshop.query.vo;

public class SimpleStoreVO {

  private Long storeId;
  private String storeName;

  private Long sellerId;
  private String storeTelephone;
  private String storeEmail;

  private int status;

  public Long getStoreId() {
    return storeId;
  }

  public SimpleStoreVO setStoreId(Long storeId) {
    this.storeId = storeId;
    return this;
  }

  public String getStoreName() {
    return storeName;
  }

  public SimpleStoreVO setStoreName(String storeName) {
    this.storeName = storeName;
    return this;
  }

  public Long getSellerId() {
    return sellerId;
  }

  public SimpleStoreVO setSellerId(Long sellerId) {
    this.sellerId = sellerId;
    return this;
  }

  public String getStoreTelephone() {
    return storeTelephone;
  }

  public SimpleStoreVO setStoreTelephone(String storeTelephone) {
    this.storeTelephone = storeTelephone;
    return this;
  }

  public String getStoreEmail() {
    return storeEmail;
  }

  public SimpleStoreVO setStoreEmail(String storeEmail) {
    this.storeEmail = storeEmail;
    return this;
  }

  public int getStatus() {
    return status;
  }

  public SimpleStoreVO setStatus(int status) {
    this.status = status;
    return this;
  }
}
