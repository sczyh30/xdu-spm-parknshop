package io.spm.parknshop.product.service;

import io.spm.parknshop.product.domain.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Interface of product service.
 *
 * @author Eric Zhao
 */
public interface ProductService {

  Mono<Long> addOrModifyProduct(Product product);

  Mono<Optional<Product>> getById(Long id);

  Flux<Product> getByStoreId(Long storeId);

  Mono<Void> remove(Long id);
}