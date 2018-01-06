package io.spm.parknshop.query.vo.ad;

import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.user.domain.User;

public class ProductAdvertisementVO extends AdvertisementVO {

  private Product product;
  private Store store;
  private User seller;

  public ProductAdvertisementVO() {}

  public ProductAdvertisementVO(Product product, Store store, User seller) {
    this.product = product;
    this.store = store;
    this.seller = seller;
  }

  public Product getProduct() {
    return product;
  }

  public ProductAdvertisementVO setProduct(Product product) {
    this.product = product;
    return this;
  }

  public Store getStore() {
    return store;
  }

  public ProductAdvertisementVO setStore(Store store) {
    this.store = store;
    return this;
  }

  public User getSeller() {
    return seller;
  }

  public ProductAdvertisementVO setSeller(User seller) {
    this.seller = seller;
    return this;
  }
}
