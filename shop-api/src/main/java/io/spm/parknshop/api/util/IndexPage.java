package io.spm.parknshop.api.util;

import io.spm.parknshop.category.domain.Category;
import io.spm.parknshop.product.domain.ProductVO;

import java.util.List;

public class IndexPage {

  private List<Category> categories;
  private List<ProductVO> products;

  public List<Category> getCategories() {
    return categories;
  }

  public IndexPage setCategories(List<Category> categories) {
    this.categories = categories;
    return this;
  }

  public List<ProductVO> getProducts() {
    return products;
  }

  public IndexPage setProducts(List<ProductVO> products) {
    this.products = products;
    return this;
  }
}
