package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.IndexPage;
import io.spm.parknshop.catalog.service.CatalogService;
import io.spm.parknshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class IndexApiController {

  @Autowired
  private CatalogService catalogService;
  @Autowired
  private ProductService productService;

  @GetMapping("/index")
  public Mono<IndexPage> apiIndex() {
    return catalogService.getAll().collectList()
      .flatMap(catalogs -> productService.getRecentProducts(50)
        .collectList()
        .map(products -> new IndexPage().setCatalogs(catalogs).setProducts(products))
      );
  }
}
