package io.spm.parknshop.favorite.service;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.favorite.domain.FavoriteRelation;
import io.spm.parknshop.favorite.domain.FavoriteType;
import io.spm.parknshop.favorite.repository.FavoriteRepository;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Eric Zhao
 * @author four
 */
@Service
public class FavoriteServiceImpl implements FavoriteService {

  @Autowired
  private FavoriteRepository favoriteRepository;
  @Autowired
  private ProductService productService;
  @Autowired
  private StoreService storeService;

  @Override
  public Mono<FavoriteRelation> addFavoriteProduct(Long userId, Long productId) {
    return addFavoriteInternal(new FavoriteRelation().setFavoriteType(FavoriteType.PRODUCT)
      .setTargetId(productId).setUserId(userId)
    );
  }

  @Override
  public Mono<FavoriteRelation> addFavoriteStore(Long userId, Long storeId) {
    return addFavoriteInternal(new FavoriteRelation().setFavoriteType(FavoriteType.STORE)
      .setTargetId(storeId).setUserId(userId)
    );
  }

  private Mono<?> checkProductExists(Long productId) {
    return productService.getById(productId)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.PRODUCT_NOT_EXIST, "Product does not exist")));
  }

  private Mono<FavoriteRelation> saveFavorite(FavoriteRelation favoriteRelation) {
    return async(() -> favoriteRepository.save(favoriteRelation));
  }

  private Mono<FavoriteRelation> addFavoriteInternal(/*@NonNull*/ FavoriteRelation favorite) {
    if (Objects.isNull(favorite.getUserId()) || favorite.getUserId() <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("userId"));
    }
    Long targetId = favorite.getTargetId();
    if (Objects.isNull(targetId) || targetId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("targetId"));
    }
    switch (favorite.getFavoriteType()) {
      case FavoriteType.STORE:
        // TODO: Check store exists!
        return saveFavorite(favorite);
      case FavoriteType.PRODUCT:
        return checkProductExists(targetId)
          .flatMap(v -> saveFavorite(favorite));
      default:
        return Mono.error(ExceptionUtils.invalidParam("Unknown favorite target"));
    }
  }

  @Override
  public Flux<FavoriteRelation> getByUserId(Long userId) {
    if (Objects.isNull(userId) || userId < 0) {
      return Flux.error(ExceptionUtils.invalidParam("userId"));
    }
    return asyncIterable(() -> favoriteRepository.getByUserId(userId));
  }

  @Override
  public Flux<FavoriteRelation> getByStoreId(Long targetId) {
    if (Objects.isNull(targetId) || targetId < 0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> favoriteRepository.getByFavoriteTypeAndTargetId(FavoriteType.STORE, targetId));
  }

  @Override
  public Flux<FavoriteRelation> getByProductId(Long targetId) {
    if (Objects.isNull(targetId) || targetId < 0) {
      return Flux.error(ExceptionUtils.invalidParam("productId"));
    }
    return asyncIterable(() -> favoriteRepository.getByFavoriteTypeAndTargetId(FavoriteType.PRODUCT, targetId));
  }

  @Override
  public Mono<Long> removeFromFavorite(Long id) {
    if (Objects.isNull(id) || id < 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> favoriteRepository.deleteById(id));
  }

  @Override
  public Mono<Boolean> checkUserLikeProduct(Long userId, Long productId) {
    // TODO: check.
    return async(() -> favoriteRepository.checkUserFavorite(userId, FavoriteType.PRODUCT, productId))
      .map(Optional::isPresent);
  }

  @Override
  public Mono<Long> removeProductFavorite(Long userId, Long productId) {
    // TODO: check.
    return asyncExecute(() -> favoriteRepository.cancelFavorite(userId, FavoriteType.PRODUCT, productId));
  }
}
