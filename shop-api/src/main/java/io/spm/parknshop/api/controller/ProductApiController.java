package io.spm.parknshop.api.controller;

import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.service.ProductService;
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
  public Mono<ProductVO> apiGetById(@PathVariable("id") Long id) {
    return productService.getProductVO(id)
      .filter(Optional::isPresent)
      .map(Optional::get);
  }
}
