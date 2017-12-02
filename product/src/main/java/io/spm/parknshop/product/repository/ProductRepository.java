package io.spm.parknshop.product.repository;

import io.spm.parknshop.product.domain.Product;

import java.util.Optional;

/**
 * Interface of product repository.
 *
 * @author Eric Zhao
 */
public interface ProductRepository {

  Optional<Product> getById(long id);
}
