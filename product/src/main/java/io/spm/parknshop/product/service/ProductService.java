package io.spm.parknshop.product.service;

import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.domain.ProductVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Interface of product service.
 *
 * @author Eric Zhao
 */
public interface ProductService {

  Mono<Product> add(Product product);

  Mono<Product> modify(Long productId, Product product);

  Mono<Optional<Product>> getById(Long id);

  Flux<ProductVO> getByStoreId(Long storeId);

  Flux<Product> getByCatalogId(Long catalogId);

  Flux<ProductVO> getVOByCategoryId(Long catalogId);

  Flux<ProductVO> getRecentProducts(int number);

  Mono<Long> remove(Long id);

  Flux<ProductVO> searchProductByKeyword(String keyword);

  Mono<Optional<ProductVO>> getProductVO(Long id);
}