package io.spm.parknshop.api.controller;

import io.spm.parknshop.catalog.domain.Catalog;
import io.spm.parknshop.catalog.service.CatalogService;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.service.ProductService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class CategoryController {

  @Autowired
  private ProductService productService;
  @Autowired
  private CatalogService catalogService;

  @GetMapping("/catalog/{id}/products")
  public Mono<?> apiGetProductsByCatalog(@PathVariable("id") Long id) {
    return catalogService.getById(id)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .flatMap(catalog -> productService.getVOByCatalogId(id)
        .collectList()
        .map(products -> wrapCatalogProduct(catalog, products))
      );
  }

  private Map<String, Object> wrapCatalogProduct(Catalog catalog, List<ProductVO> products) {
    Map<String, Object> map = new HashMap<>(4);
    map.put("catalog", catalog);
    map.put("products", products);
    return map;
  }

  @GetMapping("/catalog/{id}")
  public Mono<Catalog> apiGetCatalog(@PathVariable("id") Long id) {
    return catalogService.getById(id)
      .filter(Optional::isPresent)
      .map(Optional::get);
  }
  @GetMapping("/categories/all")
  public Publisher<Catalog> apiGetAll() {
    return catalogService.getAll();
  }
}
