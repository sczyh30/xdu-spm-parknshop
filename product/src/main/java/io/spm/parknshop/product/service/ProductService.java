package io.spm.parknshop.product.service;

import io.spm.parknshop.product.domain.Product;
import rx.Completable;
import rx.Observable;
import rx.Single;

import java.util.Optional;

/**
 * Interface of product service.
 *
 * @author Eric Zhao
 */
public interface ProductService {

  Single<Optional<Product>> getById(Long id);

  Observable<Product> getByStoreId(Long storeId);

  Completable remove(Long id);
}