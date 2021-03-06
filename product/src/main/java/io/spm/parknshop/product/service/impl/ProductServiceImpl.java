package io.spm.parknshop.product.service.impl;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.inventory.domain.Inventory;
import io.spm.parknshop.inventory.repository.InventoryRepository;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.domain.ProductStatus;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private InventoryRepository inventoryRepository;

  @Override
  public Mono<Product> add(Product product) {
    if (!isValidNewProduct(product)) {
      return Mono.error(ExceptionUtils.invalidParam("product"));
    }
    if (Objects.nonNull(product.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("product ID should not be provided"));
    }
    return async(() -> addProductInternal(product));
  }

  @Transactional
  protected Product addProductInternal(Product product) {
    Product r = productRepository.save(product);
    Inventory inventory = new Inventory().setId(r.getId())
      .setAmount(0);
    inventoryRepository.save(inventory);
    return r;
  }

  @Override
  public Mono<Product> modify(Long productId, Product product) {
    if (Objects.isNull(productId) || productId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("productId"));
    }
    if (!isValidProduct(product)) {
      return Mono.error(ExceptionUtils.invalidParam("product"));
    }
    if (!productId.equals(product.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> productRepository.save(product.setGmtModified(new Date())));
  }

  @Override
  public Mono<Product> filterNormal(/*@NonNull*/ Product product) {
    if (ProductStatus.isRemoved(product.getStatus())) {
      return Mono.error(new ServiceException(PRODUCT_REMOVED, "This product has been deleted"));
    }
    if (ProductStatus.isAvailable(product.getStatus())) {
      return Mono.just(product);
    }
    return Mono.error(new ServiceException(PRODUCT_UNAVAILABLE, "This product is not unavailable"));
  }

  @Override
  public Mono<Product> getById(final Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> productRepository.findById(id))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.PRODUCT_NOT_EXIST, "Product does not exist")));
  }

  @Override
  public Flux<Product> getByStoreId(Long storeId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> productRepository.getByStoreId(storeId));
  }

  @Override
  public Flux<Product> getByCatalogId(Long catalogId) {
    if (Objects.isNull(catalogId) || catalogId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("catalogId"));
    }
    return asyncIterable(() -> productRepository.getByCatalogId(catalogId));
  }

  @Override
  public Mono<Long> remove(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> productRepository.markAsDeleted(id));
  }

  @Transactional
  protected void removeInternal(long id) {
    productRepository.deleteById(id);
    inventoryRepository.deleteById(id);
  }

  @Override
  public Mono<String> modifyPicUrl(String url, Long id) {
    return asyncExecute(() -> productRepository.modifyProductPicUrl(url, id))
      .map(e -> url);
  }

  private boolean isValidNewProduct(final Product product) {
    return Optional.ofNullable(product)
      .map(e -> product.getCatalogId())
      .map(e -> product.getName())
      .map(e -> product.getStoreId())
      .map(e -> product.getDescription())
      .isPresent();
  }

  private boolean isValidProduct(final Product product) {
    return Optional.ofNullable(product)
      .map(e -> product.getId())
      .map(e -> product.getCatalogId())
      .map(e -> product.getName())
      .map(e -> product.getStoreId())
      .map(e -> product.getDescription())
      .isPresent();
  }
}
