package io.spm.parknshop.cart.repository;

import io.spm.parknshop.cart.domain.SimpleCartProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

  public Flux<SimpleCartProduct> getCart(/*@NonNull*/ Long userId) {
    return template.<String, SimpleCartProduct>opsForHash()
      .values(getCartKey(userId));
  }

  public Mono<Boolean> clearCart(/*@NonNull*/ Long userId) {
    return template.opsForHash().delete(getCartKey(userId));
  }

  public Mono<Boolean> putCart(/*@NonNull*/ Long userId, List<SimpleCartProduct> products) {
    if (products == null || products.isEmpty()) {
      return Mono.just(true);
    }
    Map<String, SimpleCartProduct> map = new HashMap<>(products.size());
    products.forEach(e -> map.putIfAbsent(getProductKey(e.getId()), e));
    return template.opsForHash()
      .putAll(getCartKey(userId), map);
  }

  private String getProductKey(Long productId) {
    return "p_" + productId;
  }

  private String getCartKey(Long userId) {
    return "carts:cart_" + userId;
  }
}
