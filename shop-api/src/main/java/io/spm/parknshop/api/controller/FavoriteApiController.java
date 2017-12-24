package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.favorite.domain.FavoriteRelation;
import io.spm.parknshop.favorite.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class FavoriteApiController {

  @Autowired
  private FavoriteService favoriteService;

  @PostMapping("/favorite/add_favorite_product/{productId}")
  public Mono<FavoriteRelation> apiAddFavoriteProduct(ServerWebExchange exchange, @PathVariable("productId") Long productId) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> favoriteService.addFavoriteProduct(userId, productId));
  }

  @PostMapping("/favorite/add_favorite_store/{storeId}")
  public Mono<FavoriteRelation> apiAddFavoriteStore(ServerWebExchange exchange, @PathVariable("storeId") Long storeId) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> favoriteService.addFavoriteStore(userId, storeId));
  }

  @PostMapping("/favorite/cancel_product_favorite/{productId}")
  public Mono<Long> apiCancelFavorite(ServerWebExchange exchange, @PathVariable("productId") Long productId) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> favoriteService.removeProductFavorite(userId, productId));
  }

}
