package io.spm.parknshop.favorite.service;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.favorite.domain.FavoriteRelation;
import io.spm.parknshop.favorite.domain.FavoriteType;
import io.spm.parknshop.favorite.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;

import java.util.Objects;

/**
 * @author Eric Zhao
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

  @Autowired
  private FavoriteRepository favoriteRepository;

  @Override
  public Mono<FavoriteRelation> addFavorite(FavoriteRelation favorite) {
    if(Objects.isNull(favorite)) {
      return Mono.error(ExceptionUtils.invalidParam("favorite"));
    }
    if(Objects.isNull(favorite.getUserId())) {
      return Mono.error(ExceptionUtils.invalidParam("userId"));
    }

    return async(() -> favoriteRepository.save(favorite));
  }

  @Override
  public Flux<FavoriteRelation> getByUserId(Long userId) {
    if(Objects.isNull(userId) || userId<0) {
      return Flux.error(ExceptionUtils.invalidParam("userId"));
    }
    return asyncIterable(() -> favoriteRepository.getByUserId(userId));
  }

  @Override
  public Flux<FavoriteRelation> getByStoreId( Long targetId) {
    if(Objects.isNull(targetId) || targetId<0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> favoriteRepository.getByFavoriteTypeAndTargetId(FavoriteType.STORE, targetId));
  }
  @Override
  public Flux<FavoriteRelation> getByProductId( Long targetId) {
      if(Objects.isNull(targetId) || targetId<0) {
        return Flux.error(ExceptionUtils.invalidParam("productId"));
      }
      return asyncIterable(() -> favoriteRepository.getByFavoriteTypeAndTargetId(FavoriteType.PRODUCT, targetId));
  }

  @Override
  public Mono<Long> removeFromFavorite(Long id) {
    if(Objects.isNull(id) || id<0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> favoriteRepository.deleteById(id));
  }


}
