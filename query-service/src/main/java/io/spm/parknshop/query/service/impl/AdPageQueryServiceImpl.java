package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.advertisement.domain.AdType;
import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.service.AdvertisementService;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.query.service.AdPageQueryService;
import io.spm.parknshop.query.vo.ad.AdvertisementVO;
import io.spm.parknshop.query.vo.ad.ProductAdvertisementVO;
import io.spm.parknshop.query.vo.ad.ShopAdvertisementVO;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.exception.ErrorConstants.*;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * @author Eric Zhao
 */
@Service
public class AdPageQueryServiceImpl implements AdPageQueryService {

  @Autowired
  private AdvertisementService advertisementService;

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private UserRepository userRepository;

  @Override
  public Mono<AdvertisementVO> getAdvertisementById(Long id) {
    return advertisementService.getById(id)
      .flatMap(this::getAdInternal);
  }

  @Override
  public Flux<AdvertisementVO> getAdvertisementBySellerId(Long sellerId) {
    return advertisementService.getBySeller(sellerId)
      .concatMap(this::getAdInternal);
  }

  @Override
  public Flux<AdvertisementVO> getFullList() {
    return advertisementService.getAll()
      .concatMap(this::getAdInternal);
  }

  private Mono<? extends AdvertisementVO> getAdInternal(/*@NonNull*/ Advertisement advertisement) {
    switch (advertisement.getAdType()) {
      case AdType.AD_PRODUCT:
        return getProductAdvertisement(advertisement.getAdTarget(), advertisement.getAdOwner())
          .map(e -> e.setAd(advertisement));
      case AdType.AD_STORE:
        return getShopAdvertisement(advertisement.getAdTarget(), advertisement.getAdOwner())
          .map(e -> e.setAd(advertisement));
      default:
        return Mono.error(new ServiceException(AD_UNKNOWN_TYPE, "Unknown advertisement type"));
    }
  }

  @Override
  public Mono<ProductAdvertisementVO> getProductAdvertisement(Long productId, Long sellerId) {
    if (Objects.isNull(productId) || productId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("productId"));
    }
    return async(() -> retrieveProductAd(productId, sellerId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(PRODUCT_NOT_EXIST, "Product is not present")));
  }

  @Override
  public Mono<ShopAdvertisementVO> getShopAdvertisement(Long storeId, Long sellerId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("storeId"));
    }
    return async(() -> retrieveShopAd(storeId, sellerId));
  }

  @Transactional(readOnly = true)
  protected ShopAdvertisementVO retrieveShopAd(long storeId, Long sellerId) {
    Store store = storeRepository.findById(storeId).orElse(Store.deletedStore(storeId));
    User seller = userRepository.getSellerById(sellerId).orElse(User.deletedUser(sellerId));
    return new ShopAdvertisementVO(store, seller);
  }

  @Transactional(readOnly = true)
  protected Optional<ProductAdvertisementVO> retrieveProductAd(long productId, Long sellerId) {
    return productRepository.findByIdWithDeleted(productId)
      .map(product -> {
        Store store = storeRepository.findById(product.getStoreId()).orElse(Store.deletedStore(product.getStoreId()));
        User seller = userRepository.getSellerById(sellerId).orElse(User.deletedUser(sellerId));
        return new ProductAdvertisementVO(product, store, seller);
      });
  }
}
