package io.spm.parknshop.advertisement.service;

import io.spm.parknshop.advertisement.domain.Advertisement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface AdvertisementService {

  Mono<Advertisement> getById(Long id);

  Flux<Advertisement> getBySeller(Long sellerId);

  Flux<Advertisement> getAll();

  Mono<Advertisement> addNewAdvertisement(Advertisement advertisement);

  Flux<Advertisement> getCurrentProductAd();

  Flux<Advertisement> getCurrentShopAd();
}
