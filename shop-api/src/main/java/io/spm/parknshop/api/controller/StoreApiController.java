package io.spm.parknshop.api.controller;


import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/api/v1/")
public class StoreApiController {

  @Autowired
  private ProductService productService;

  @GetMapping("/store/{id}/products")
  private Flux<Product> apiGetProductsById(@PathVariable("id") Long id) {
    return productService.getByStoreId(id);
  }
}
