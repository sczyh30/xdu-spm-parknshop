package io.spm.parknshop.product.service;

import io.spm.parknshop.product.domain.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface of product service.
 *
 * @author Eric Zhao
 */
public interface ProductService {

  Mono<Product> add(Product product);

  Mono<Product> modify(Long productId, Product product);

  Mono<Product> getById(Long id);

  Mono<Product> filterNormal(Product product);

  Flux<Product> getByStoreId(Long storeId);

  Flux<Product> getByCatalogId(Long catalogId);

  Mono<Long> remove(Long id);

  Mono<String> modifyPicUrl(String url, Long id);
}