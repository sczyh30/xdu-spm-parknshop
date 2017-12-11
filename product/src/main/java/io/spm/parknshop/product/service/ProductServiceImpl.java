package io.spm.parknshop.product.service;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.inventory.domain.Inventory;
import io.spm.parknshop.inventory.repository.InventoryRepository;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.product.repository.ProductVORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class ProductServiceImpl implements ProductService {

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ProductVORepository productVORepository;

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
  public Mono<Optional<Product>> getById(final Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> productRepository.findById(id));
  }

  @Override
  public Mono<Optional<ProductVO>> getProductVO(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> productVORepository.getProductVO(id));
  }

  @Override
  public Flux<ProductVO> getByStoreId(Long storeId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> productVORepository.getByStoreId(storeId));
  }

  @Override
  public Flux<Product> getByCatalogId(Long catalogId) {
    if (Objects.isNull(catalogId) || catalogId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("catalogId"));
    }
    return asyncIterable(() -> productRepository.getByCatalogId(catalogId));
  }

  @Override
  public Flux<ProductVO> getVOByCatalogId(Long catalogId) {
    if (Objects.isNull(catalogId) || catalogId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("catalogId"));
    }
    return asyncIterable(() -> productVORepository.searchProductVOByCatalog(catalogId));
  }

  @Override
  public Flux<ProductVO> getRecentProducts(int number) {
    if (number <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("number"));
    }
    return asyncIterable(() -> productVORepository.getNRecentProductVO(number));
  }

  @Override
  public Mono<Long> remove(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> removeInternal(id));
  }

  @Transactional
  protected void removeInternal(long id) {
    productRepository.deleteById(id);
    inventoryRepository.deleteById(id);
  }

  @Override
  public Flux<ProductVO> searchProductByKeyword(String keyword) {
    if(Objects.isNull(keyword) || "".equals(keyword)) {
      return Flux.error(ExceptionUtils.invalidParam("keyword"));
    }
    return asyncIterable(() -> productVORepository.searchProductVOByKeyword(keyword));
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
