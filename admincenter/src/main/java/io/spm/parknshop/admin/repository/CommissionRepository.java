package io.spm.parknshop.admin.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CommissionRepository {

  private static String COMMISIONKEY = "commission";

  @Autowired
  private ReactiveRedisTemplate<String, String> template;

  public Mono<Boolean> setCommission(Double commission) {
    return template.opsForValue()
        .set(COMMISIONKEY, String.valueOf(commission));
  }

  public Mono<Double> getCommission() {
    return template.opsForValue()
        .get(COMMISIONKEY)
        .map(Double::valueOf)
        .switchIfEmpty(Mono.just(2D));
  }
}
