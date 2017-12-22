package io.spm.parknshop.query.vo;

public class SimpleStoreVO {

  private Long storeId;
  private String storeName;

  private Long sellerId;
  private String sellerTelephone;

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

  public String getSellerTelephone() {
    return sellerTelephone;
  }

  public SimpleStoreVO setSellerTelephone(String sellerTelephone) {
    this.sellerTelephone = sellerTelephone;
    return this;
  }
}
