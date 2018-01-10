package io.spm.parknshop.query.vo.ad;

import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.user.domain.User;

public class ShopAdvertisementVO extends AdvertisementVO {

  private Store store;
  private User seller;

  public ShopAdvertisementVO() {}

  public ShopAdvertisementVO(Store store, User seller) {
    this.store = store;
    this.seller = seller;
  }

  public Store getStore() {
    return store;
  }

  public ShopAdvertisementVO setStore(Store store) {
    this.store = store;
    return this;
  }

  public User getSeller() {
    return seller;
  }

  public ShopAdvertisementVO setSeller(User seller) {
    this.seller = seller;
    return this;
  }
}
