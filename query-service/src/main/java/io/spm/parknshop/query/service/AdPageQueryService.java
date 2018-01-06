package io.spm.parknshop.query.service;

import io.spm.parknshop.query.vo.ad.AdvertisementVO;
import io.spm.parknshop.query.vo.ad.ProductAdvertisementVO;
import io.spm.parknshop.query.vo.ad.ShopAdvertisementVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface AdPageQueryService {

  Mono<AdvertisementVO> getAdvertisementById(Long id);

  Flux<AdvertisementVO> getAdvertisementBySellerId(Long sellerId);

  Flux<AdvertisementVO> getFullList();

  Mono<ProductAdvertisementVO> getProductAdvertisement(Long productId, Long sellerId);

  Mono<ShopAdvertisementVO> getShopAdvertisement(Long storeId, Long sellerId);
}
