package io.spm.parknshop.query.vo;

import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.user.domain.User;

/**
 * @author Eric Zhao
 */
public class StoreVO {

  private Long id;
  private Store store;
  private User seller;

  private Integer productAmount;
  private Integer orderAmount;

  public StoreVO() {}

  public StoreVO(Long id, Store store, User seller) {
    this.id = id;
    this.store = store;
    this.seller = seller;
  }

  public Long getId() {
    return id;
  }

  public StoreVO setId(Long id) {
    this.id = id;
    return this;
  }

  public Store getStore() {
    return store;
  }

  public StoreVO setStore(Store store) {
    this.store = store;
    return this;
  }

  public User getSeller() {
    return seller;
  }

  public StoreVO setSeller(User seller) {
    this.seller = seller;
    return this;
  }

  public Integer getProductAmount() {
    return productAmount;
  }

  public StoreVO setProductAmount(Integer productAmount) {
    this.productAmount = productAmount;
    return this;
  }

  public Integer getOrderAmount() {
    return orderAmount;
  }

  public StoreVO setOrderAmount(Integer orderAmount) {
    this.orderAmount = orderAmount;
    return this;
  }
}
