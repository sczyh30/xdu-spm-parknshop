package io.spm.parknshop.api.controller;

import io.spm.parknshop.cart.domain.CartEvent;
import io.spm.parknshop.cart.domain.ShoppingCart;
import io.spm.parknshop.cart.service.CartService;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping("/cart/update_cart")
  public Mono<ShoppingCart> apiUpdateCart(ServerWebExchange exchange, @RequestBody CartEvent cartEvent) {
    return cartService.updateCart(1L, cartEvent);
    /*return exchange.getPrincipal()
        .flatMap(this::extractIdFromPrincipal)
        .flatMap(id -> cartService.addCart(id, simpleCartProduct))
        .switchIfEmpty(Mono.error(ExceptionUtils.authNoPermission()));*/
  }

  @GetMapping("/cart")
  public Mono<ShoppingCart> apiGetCart(ServerWebExchange exchange) {
    return cartService.getCartForUser(1L);
  }

  private Mono<Long> extractIdFromPrincipal(Object principal) {
    if (principal instanceof User) {
      return Mono.just(((User) principal).getId());
    } else {
      return Mono.error(ExceptionUtils.authNoPermission());
    }
  }
}
