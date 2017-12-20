package io.spm.parknshop.cart.service;

import io.spm.parknshop.cart.domain.CartEvent;
import io.spm.parknshop.cart.domain.SimpleCartProduct;
import io.spm.parknshop.cart.domain.ShoppingCart;
import reactor.core.publisher.Mono;

public interface CartService {

  Mono<ShoppingCart> updateCart(Long userId, CartEvent cartEvent);

  Mono<ShoppingCart> getCartForUser(Long userId);
}
