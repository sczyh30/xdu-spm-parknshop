package io.spm.parknshop.seller.repository;

import io.spm.parknshop.seller.domain.StoreApplyDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Repository
public class StoreApplyRepository {

  private static final String APPLY_KEY = "store_apply";

  @Autowired
  private ReactiveRedisTemplate<String, StoreApplyDO> template;

  public Mono<StoreApplyDO> getPendingStoreBySellerId(long sellerId) {
    return template.<String, StoreApplyDO>opsForHash()
      .get(APPLY_KEY, getKey(sellerId));
  }

  public Mono<Boolean> putStoreApply(long sellerId, StoreApplyDO storeApplyDO) {
    return template.opsForHash()
        .put(APPLY_KEY, getKey(sellerId), storeApplyDO);
  }

  public Flux<StoreApplyDO> getApplyStoreList() {
    return template.<String, StoreApplyDO>opsForHash()
        .entries(APPLY_KEY)
        .map(Map.Entry::getValue);
  }

  public Mono<Long> deleteOneApply(Long sellerId) {
    return template.opsForHash().remove(APPLY_KEY,getKey(sellerId));
  }

  private String getKey(Long sellerId) {
    return "seller_" + sellerId;
  }
}
