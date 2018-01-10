package io.spm.parknshop.product.service;

import io.spm.parknshop.product.domain.ProductVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface ProductQueryService {

  Flux<ProductVO> getUserFavoriteProducts(Long userId);

  Flux<ProductVO> getByStoreId(Long storeId);

  Flux<ProductVO> getByCategoryId(Long catalogId);

  Flux<ProductVO> getRecentProducts(int number);

  Flux<ProductVO> searchProductByKeyword(String keyword);

  Mono<Optional<ProductVO>> getProduct(Long id);
}
