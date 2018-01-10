package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.product.domain.ProductStatus;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.product.service.ProductQueryService;
import io.spm.parknshop.query.service.impl.ProductDetailDataService;
import io.spm.parknshop.query.vo.ProductDetailUserVO;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static io.spm.parknshop.common.exception.ErrorConstants.*;

@RestController
@RequestMapping("/api/v1/")
public class ProductApiController {

  @Autowired
  private ProductService productService;
  @Autowired
  private ProductDetailDataService productDetailDataService;
  @Autowired
  private ProductQueryService productQueryService;

  @GetMapping("/product/{id}")
  public Mono<ProductVO> apiGetById(@PathVariable("id") Long id) {
    return productQueryService.getProduct(id)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(PRODUCT_NOT_EXIST, "The product does not exist")))
      .filter(e -> !ProductStatus.isRemoved(e.getProduct().getStatus()))
      .switchIfEmpty(Mono.error(new ServiceException(PRODUCT_DELETED, "The product has been deleted")));
  }

  @GetMapping("/product/user_detail_data/{id}")
  public Mono<ProductDetailUserVO> apiGetProductUserDetailById(ServerWebExchange exchange, @PathVariable("id") Long id) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> productDetailDataService.getData(userId, id));
  }


  @GetMapping("/products/my_favorite")
  public /*Flux*/ Publisher<ProductVO> apiGetFavoriteProductsByUser(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(userId -> productQueryService.getUserFavoriteProducts(userId));
  }
}
