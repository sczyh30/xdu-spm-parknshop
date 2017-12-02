package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.Result;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ProductApiController {

  @Autowired
  private ProductService productService;

  @GetMapping("/product/{id}")
  private Mono<Result<Product>> apiGetById(@PathVariable("id") Long id) {
    return productService.getById(id)
      .filter(Optional::isPresent)
      .map(e -> Result.success(e.get()))
      .switchIfEmpty(Mono.just(Result.notFound()));
  }
}
