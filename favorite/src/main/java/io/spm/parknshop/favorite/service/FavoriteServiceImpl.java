package io.spm.parknshop.favorite.service;

import io.spm.parknshop.favorite.domain.FavoriteRelation;
import io.spm.parknshop.favorite.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

  @Autowired
  private FavoriteRepository favoriteRepository;

  @Override
  public Mono<FavoriteRelation> addFavorite(FavoriteRelation favorite) {
    return null;
  }

  @Override
  public Flux<FavoriteRelation> getByUserId(Long userId) {
    return null;
  }

  @Override
  public Flux<FavoriteRelation> getByTargetId(Integer type, Long targetId) {
    return null;
  }

  @Override
  public Mono<Long> removeFromFavorite(Long id) {
    return null;
  }
}
