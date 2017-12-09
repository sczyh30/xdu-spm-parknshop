package io.spm.parknshop.cart.service;

import io.spm.parknshop.cart.domain.CartProductDO;
import io.spm.parknshop.cart.domain.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CartServiceImpl implements CartService {

  @Autowired
  private ReactiveRedisTemplate<String, CartProductDO> template;

  @Override
  public Mono<Long> addCart(Long userId, CartProductDO cartProduct) {
    return template.opsForList().leftPush(getKey(userId), cartProduct);
  }

  @Override
  public Mono<ShoppingCart> getCartForUser(Long userId) {
    return null;
  }

  private String getKey(Long userId) {
    return "cart_" + userId;
  }
}
