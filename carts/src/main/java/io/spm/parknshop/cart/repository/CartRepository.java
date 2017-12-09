package io.spm.parknshop.cart.repository;

import io.spm.parknshop.cart.domain.SimpleCartProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CartRepository {

  @Autowired
  private ReactiveRedisTemplate<String, SimpleCartProduct> template;

  public Mono<Boolean> putCartProduct(/*@NonNull*/ Long userId, SimpleCartProduct cartProduct) {
    return template.opsForHash()
        .put(getCartKey(userId), getProductKey(cartProduct.getId()), cartProduct);
  }

  public Mono<Long> deleteCartProduct(/*@NonNull*/ Long userId, /*@NonNull*/ Long productId) {
    return template.opsForHash()
        .remove(getCartKey(userId), getProductKey(productId));
  }

  public Mono<SimpleCartProduct> getCartProduct(/*@NonNull*/ Long userId, /*@NonNull*/ Long productId) {
    return template.<String, SimpleCartProduct>opsForHash()
        .get(getCartKey(userId), getProductKey(productId));
  }

  private String getProductKey(Long productId) {
    return "p_" + productId;
  }

  private String getCartKey(Long userId) {
    return "carts:cart_" + userId;
  }
}
