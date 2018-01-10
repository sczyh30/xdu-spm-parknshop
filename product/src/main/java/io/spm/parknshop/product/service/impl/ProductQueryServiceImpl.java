package io.spm.parknshop.product.service.impl;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.repository.ProductQueryRepository;
import io.spm.parknshop.product.service.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * @author Eric Zhao
 */
@Service
public class ProductQueryServiceImpl implements ProductQueryService {

  @Autowired
  private ProductQueryRepository productQueryRepository;

  @Override
  public Flux<ProductVO> getUserFavoriteProducts(Long userId) {
    if (Objects.isNull(userId) || userId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("userId"));
    }
    return asyncIterable(() -> productQueryRepository.getByUserFavorite(userId));
  }

  @Override
  public Mono<Optional<ProductVO>> getProduct(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> productQueryRepository.getProductVOWithDeleted(id));
  }

  @Override
  public Flux<ProductVO> getByStoreId(Long storeId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> productQueryRepository.getByStoreId(storeId));
  }

  @Override
  public Flux<ProductVO> getByCategoryId(Long catalogId) {
    if (Objects.isNull(catalogId) || catalogId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("catalogId"));
    }
    return asyncIterable(() -> productQueryRepository.searchProductVOByCatalog(catalogId));
  }

  @Override
  public Flux<ProductVO> getRecentProducts(int number) {
    if (number <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("number"));
    }
    return asyncIterable(() -> productQueryRepository.getNRecentProductVO(number));
  }

  @Override
  public Flux<ProductVO> searchProductByKeyword(String keyword) {
    if(Objects.isNull(keyword) || "".equals(keyword)) {
      return Flux.error(ExceptionUtils.invalidParam("keyword"));
    }
    return asyncIterable(() -> productQueryRepository.searchProductVOByKeyword(keyword));
  }
}
