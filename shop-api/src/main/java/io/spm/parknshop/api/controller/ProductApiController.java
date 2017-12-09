package io.spm.parknshop.api.controller;

import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.service.ProductService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ProductApiController {

  @Autowired
  private ProductService productService;

  @GetMapping("/product/{id}")
  public Mono<Product> apiGetById(@PathVariable("id") Long id) {
    return productService.getById(id)
      .filter(Optional::isPresent)
      .map(Optional::get);
  }

}
