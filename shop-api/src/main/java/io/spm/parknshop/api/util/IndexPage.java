package io.spm.parknshop.api.util;

import io.spm.parknshop.catalog.domain.Catalog;
import io.spm.parknshop.product.domain.ProductVO;

import java.util.List;

public class IndexPage {

  private List<Catalog> catalogs;
  private List<ProductVO> products;

  public List<Catalog> getCatalogs() {
    return catalogs;
  }

  public IndexPage setCatalogs(List<Catalog> catalogs) {
    this.catalogs = catalogs;
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
