package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.query.vo.ad.ProductAdvertisementVO;
import io.spm.parknshop.query.vo.ad.ShopAdvertisementVO;
import io.spm.parknshop.store.repository.StoreRepository;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.exception.ErrorConstants.*;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * @author Eric Zhao
 */
@Service
public class AdPageQueryService {

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private UserRepository userRepository;

  public Mono<ProductAdvertisementVO> getProductAdvertisement(Long productId) {
    if (Objects.isNull(productId) || productId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("productId"));
    }
    return async(() -> retrieveProductAd(productId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(PRODUCT_NOT_EXIST, "Product or related shop is not present")));
  }

  public Mono<ShopAdvertisementVO> getShopAdvertisement(Long storeId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("storeId"));
    }
    return async(() -> retrieveShopAd(storeId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(STORE_NOT_EXIST, "Shop is not present")));
  }

  @Transactional(readOnly = true)
  private Optional<ShopAdvertisementVO> retrieveShopAd(long storeId) {
    return storeRepository.findById(storeId)
      .flatMap(store -> userRepository.getSellerById(store.getSellerId())
        .map(seller -> new ShopAdvertisementVO(store, seller))
      );
  }

  @Transactional(readOnly = true)
  private Optional<ProductAdvertisementVO> retrieveProductAd(long productId) {
    return productRepository.findByIdWithDeleted(productId)
      .flatMap(product -> storeRepository.findById(product.getStoreId())
        .flatMap(store -> userRepository.getSellerById(store.getSellerId())
          .map(seller -> new ProductAdvertisementVO(product, store, seller))
        ));
  }
}
