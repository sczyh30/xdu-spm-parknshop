package io.spm.parknshop.seller.domain;

import io.spm.parknshop.store.domain.Store;

import java.util.Date;

public class StoreApplyDO {

  private String id;
  private Date applyTime;
  private Store store;

  public String getId() {
    return id;
  }

  public StoreApplyDO setId(String id) {
    this.id = id;
    return this;
  }

  public Date getApplyTime() {
    return applyTime;
  }

  public StoreApplyDO setApplyTime(Date applyTime) {
    this.applyTime = applyTime;
    return this;
  }

  public Store getStore() {
    return store;
  }

  public StoreApplyDO setStore(Store store) {
    this.store = store;
    return this;
  }

  @Override
  public String toString() {
    return "StoreApplyDO{" +
        "id='" + id + '\'' +
        ", applyTime=" + applyTime +
        ", store=" + store +
        '}';
  }
}