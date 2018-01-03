package io.spm.parknshop.query.vo;

import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.store.domain.Store;

import java.util.List;

/**
 * @author Eric Zhao
 */
public class AdNewApplyPageVO {

  private Store store;
  private List<Product> products;

  private double productAdPrice;
  private double storeAdPrice;

  public Store getStore() {
    return store;
  }

  public AdNewApplyPageVO setStore(Store store) {
    this.store = store;
    return this;
  }

  public List<Product> getProducts() {
    return products;
  }

  public AdNewApplyPageVO setProducts(List<Product> products) {
    this.products = products;
    return this;
  }

  public double getProductAdPrice() {
    return productAdPrice;
  }

  public AdNewApplyPageVO setProductAdPrice(double productAdPrice) {
    this.productAdPrice = productAdPrice;
    return this;
  }

  public double getStoreAdPrice() {
    return storeAdPrice;
  }

  public AdNewApplyPageVO setStoreAdPrice(double storeAdPrice) {
    this.storeAdPrice = storeAdPrice;
    return this;
  }
}
