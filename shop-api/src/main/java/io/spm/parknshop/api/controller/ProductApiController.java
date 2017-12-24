package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.query.service.ProductDetailDataService;
import io.spm.parknshop.query.vo.ProductDetailUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class ProductApiController {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductDetailDataService productDetailDataService;

  @GetMapping("/product/{id}")
  public Mono<ProductVO> apiGetById(@PathVariable("id") Long id) {
    return productService.getProductVO(id)
      .filter(Optional::isPresent)
      .map(Optional::get);
  }

  @GetMapping("/product/user_detail_data/{id}")
  public Mono<ProductDetailUserVO> apiGetProductUserDetailById(ServerWebExchange exchange, @PathVariable("id") Long id) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> productDetailDataService.getData(userId, id));
  }
}
