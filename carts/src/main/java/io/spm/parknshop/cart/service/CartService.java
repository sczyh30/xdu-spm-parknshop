package io.spm.parknshop.cart.service;

import io.spm.parknshop.cart.domain.CartProductDO;
import io.spm.parknshop.cart.domain.ShoppingCart;
import reactor.core.publisher.Mono;

public interface CartService {

  Mono<Long> addCart(Long userId, CartProductDO cartProduct);

  Mono<ShoppingCart> getCartForUser(Long userId);
}
