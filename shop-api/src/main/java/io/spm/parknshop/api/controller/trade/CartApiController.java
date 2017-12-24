package io.spm.parknshop.api.controller.trade;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.cart.domain.CartEvent;
import io.spm.parknshop.cart.domain.ShoppingCart;
import io.spm.parknshop.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class CartApiController {

  @Autowired
  private CartService cartService;

  @PostMapping("/cart/update_cart")
  public Mono<ShoppingCart> apiUpdateCart(ServerWebExchange exchange, @RequestBody CartEvent cartEvent) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> cartService.updateCart(userId, cartEvent));
  }

  @GetMapping("/cart")
  public Mono<ShoppingCart> apiGetCart(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> cartService.getCartForUser(userId));
  }
}
