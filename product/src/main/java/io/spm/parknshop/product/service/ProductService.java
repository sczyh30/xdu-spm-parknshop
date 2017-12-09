package io.spm.parknshop.product.service;

import io.spm.parknshop.product.domain.Product;
import org.reactivestreams.Publisher;
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

  Flux<Product> getByStoreId(Long storeId);

  Mono<Long> remove(Long id);

  Publisher<Product> searchProductByKeyword(String keyword);
}