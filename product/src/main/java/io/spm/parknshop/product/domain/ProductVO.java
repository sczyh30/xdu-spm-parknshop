package io.spm.parknshop.product.domain;

import io.spm.parknshop.catalog.domain.Category;

/**
 * @author Eric Zhao
 */
public class ProductVO {

  private Product product;
  private Category category;

  private String storeName;

  private Integer inventory;

  public Product getProduct() {
    return product;
  }

  public ProductVO setProduct(Product product) {
    this.product = product;
    return this;
  }

  public Category getCategory() {
    return category;
  }

  public ProductVO setCategory(Category category) {
    this.category = category;
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
