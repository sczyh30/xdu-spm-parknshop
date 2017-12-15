package io.spm.parknshop.store.domain;

/**
 * @author Eric Zhao
 */
public class StoreVO {

  private Store store;
  private String sellerName;

  public Store getStore() {
    return store;
  }

  public StoreVO setStore(Store store) {
    this.store = store;
    return this;
  }

  public String getSellerName() {
    return sellerName;
  }

  public StoreVO setSellerName(String sellerName) {
    this.sellerName = sellerName;
    return this;
  }
}
