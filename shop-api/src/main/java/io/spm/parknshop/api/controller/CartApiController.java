package io.spm.parknshop.api.controller;

import io.spm.parknshop.cart.domain.CartEvent;
import io.spm.parknshop.cart.domain.SimpleCartProduct;
import io.spm.parknshop.cart.domain.ShoppingCart;
import io.spm.parknshop.cart.service.CartService;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
public class CartApiController {

  @Autowired
  private CartService cartService;

  @PostMapping("/cart/add_cart")
  public Mono<ShoppingCart> addCart(ServerWebExchange exchange, @RequestBody CartEvent cartEvent) {
    return cartService.addCart(1L, cartEvent);
    /*return exchange.getPrincipal()
        .flatMap(this::extractIdFromPrincipal)
        .flatMap(id -> cartService.addCart(id, simpleCartProduct))
        .switchIfEmpty(Mono.error(ExceptionUtils.authNoPermission()));*/
  }

  @PostMapping("/cart/delete_cart")
  public Mono<ShoppingCart> deleteCart(ServerWebExchange exchange, @RequestBody CartEvent cartEvent) {
    return cartService.deleteCart(1L, cartEvent);
    /*return exchange.getPrincipal()
        .flatMap(this::extractIdFromPrincipal)
        .flatMap(id -> cartService.addCart(id, simpleCartProduct))
        .switchIfEmpty(Mono.error(ExceptionUtils.authNoPermission()));*/
  }

  private Mono<Long> extractIdFromPrincipal(Object principal) {
    if (principal instanceof User) {
      return Mono.just(((User) principal).getId());
    } else {
      return Mono.error(ExceptionUtils.authNoPermission());
    }
  }
}
