package io.spm.parknshop.query.vo.ad;

import io.spm.parknshop.advertisement.domain.Advertisement;

public abstract class AdvertisementVO {

  protected Advertisement ad;
  protected String storeName;

  public Advertisement getAd() {
    return ad;
  }

  public AdvertisementVO setAd(Advertisement ad) {
    this.ad = ad;
    return this;
  }

  public String getStoreName() {
    return storeName;
  }

  public AdvertisementVO setStoreName(String storeName) {
    this.storeName = storeName;
    return this;
  }
}
