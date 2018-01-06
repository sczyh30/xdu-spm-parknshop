package io.spm.parknshop.query.vo;

import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.category.domain.Category;
import io.spm.parknshop.product.domain.ProductVO;

import java.util.List;

public class IndexPage {

  private List<Category> categories;
  private List<ProductVO> products;

  private List<Advertisement> productAdList;
  private List<Advertisement> shopAdList;

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

  public List<Advertisement> getProductAdList() {
    return productAdList;
  }

  public IndexPage setProductAdList(List<Advertisement> productAdList) {
    this.productAdList = productAdList;
    return this;
  }

  public List<Advertisement> getShopAdList() {
    return shopAdList;
  }

  public IndexPage setShopAdList(List<Advertisement> shopAdList) {
    this.shopAdList = shopAdList;
    return this;
  }
}
