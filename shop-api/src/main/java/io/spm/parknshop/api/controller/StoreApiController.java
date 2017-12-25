package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.service.StoreService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class StoreApiController {

  @Autowired
  private ProductService productService;
  @Autowired
  private StoreService storeService;

  @GetMapping("/store/{id}/products")
  public /*Flux*/ Publisher<ProductVO> apiGetProductsById(@PathVariable("id") Long id) {
    return productService.getByStoreId(id);
  }

  @PostMapping("/store/{id}/add_product")
  public Mono<Product> apiAddProductToStore(@PathVariable("id") Long id, @RequestBody Product product) {
    return productService.add(product);
  }

  @PutMapping("/store/{id}/modify_product/{productId}")
  public Mono<Product> apiModifyProduct(@PathVariable("id") Long id, @PathVariable("productId") Long productId, @RequestBody Product product) {
    return productService.modify(productId, product);
  }

  @DeleteMapping("/store/{id}/delete_product/{productId}")
  public Mono<Long> apiRemoveProductFromStore(@PathVariable("id") Long id, @PathVariable("productId") Long productId) {
    return productService.remove(productId);
  }

  @GetMapping("/store/{id}")
  public Mono<Store> apiGetStore(@PathVariable("id") Long id) {
    return storeService.getById(id)
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @PostMapping("/store/{id}")
  public Mono<Store> apiModifyStore(@PathVariable("id") Long id, @RequestBody Store store) {
    return storeService.modify(id, store);
  }

  @DeleteMapping("/store/{id}")
  public Mono<?> apiRemoveStore(@PathVariable("id") Long id) {
    return storeService.remove(id);
  }
}
