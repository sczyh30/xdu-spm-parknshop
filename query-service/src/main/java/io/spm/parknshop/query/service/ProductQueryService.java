package io.spm.parknshop.query.service;

import io.spm.parknshop.product.domain.ProductVO;
import reactor.core.publisher.Flux;

/**
 * @author Eric Zhao
 */
public interface ProductQueryService {

  Flux<ProductVO> getUserFavoriteProducts(Long userId);

}
