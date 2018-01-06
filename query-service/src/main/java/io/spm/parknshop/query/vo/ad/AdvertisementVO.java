package io.spm.parknshop.query.vo.ad;

import io.spm.parknshop.advertisement.domain.Advertisement;

public abstract class AdvertisementVO {

  protected Advertisement ad;

  public Advertisement getAd() {
    return ad;
  }

  public AdvertisementVO setAd(Advertisement ad) {
    this.ad = ad;
    return this;
  }
}
