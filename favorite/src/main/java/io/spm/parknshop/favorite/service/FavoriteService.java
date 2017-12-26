package io.spm.parknshop.favorite.service;

import io.spm.parknshop.favorite.domain.FavoriteRelation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface FavoriteService {

  Mono<FavoriteRelation> addFavoriteProduct(Long userId, Long productId);

  Mono<FavoriteRelation> addFavoriteStore(Long userId, Long storeId);

  Flux<FavoriteRelation> getByUserId(Long userId);

  Flux<FavoriteRelation> getByStoreId(Long storeId);

  Flux<FavoriteRelation> getByProductId(Long productId);

  Mono<Boolean> checkUserLikeProduct(Long userId, Long productId);

  Mono<Long> removeFromFavorite(Long id);

  Mono<Long> removeProductFavorite(Long userId, Long productId);
}
