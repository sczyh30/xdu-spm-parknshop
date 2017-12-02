package io.spm.parknshop.product.service.impl;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Completable;
import rx.Observable;
import rx.Single;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.RxAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public Single<Optional<Product>> getById(final Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Single.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> productRepository.getById(id));
  }

  @Override
  public Observable<Product> getByStoreId(Long storeId) {
    return null;
  }

  @Override
  public Completable remove(Long id) {
    return null;
  }
}
