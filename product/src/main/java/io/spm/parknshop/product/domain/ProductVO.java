package io.spm.parknshop.product.domain;

import io.spm.parknshop.catalog.domain.Catalog;

public class ProductVO {
  private Product product;
  private Catalog catalog;

  private String storeName;

  private Integer inventory;

  public Product getProduct() {
    return product;
  }

  public ProductVO setProduct(Product product) {
    this.product = product;
    return this;
  }

  public Catalog getCatalog() {
    return catalog;
  }

  public ProductVO setCatalog(Catalog catalog) {
    this.catalog = catalog;
    return this;
  }

  public String getStoreName() {
    return storeName;
  }

  public ProductVO setStoreName(String storeName) {
    this.storeName = storeName;
    return this;
  }

  public Integer getInventory() {
    return inventory;
  }

  public ProductVO setInventory(Integer inventory) {
    this.inventory = inventory;
    return this;
  }
}
