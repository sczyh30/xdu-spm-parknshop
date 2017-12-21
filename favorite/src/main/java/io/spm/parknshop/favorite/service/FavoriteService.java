package io.spm.parknshop.favorite.service;

import io.spm.parknshop.favorite.domain.FavoriteRelation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface FavoriteService {

  Mono<FavoriteRelation> addFavorite(FavoriteRelation favorite);

  Flux<FavoriteRelation> getByUserId(Long userId);

  Flux<FavoriteRelation> getByTargetId(Integer type, Long targetId);

  Mono<Long> removeFromFavorite(Long id);
}
