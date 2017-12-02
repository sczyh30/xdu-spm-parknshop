package io.spm.parknshop.product.service.impl;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public Mono<Long> addOrModifyProduct(Product product) {
    if (Objects.isNull(product)) {
      return Mono.error(ExceptionUtils.invalidParam("product"));
    }
    if (Objects.isNull(product.getId())) {
      // Add goes here.
    } else {

    }
    return null;
  }


  @Override
  public Mono<Optional<Product>> getById(final Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> productRepository.findById(id));
  }

  @Override
  public Flux<Product> getByStoreId(Long storeId) {
    return null;
  }

  @Override
  public Mono<Void> remove(Long id) {
    return null;
  }
}
