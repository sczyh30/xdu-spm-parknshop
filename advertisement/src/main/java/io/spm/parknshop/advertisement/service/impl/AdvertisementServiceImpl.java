package io.spm.parknshop.advertisement.service.impl;

import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.repository.AdvertisementRepository;
import io.spm.parknshop.advertisement.service.AdvertisementService;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.DateUtils;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

  @Autowired
  private AdvertisementRepository advertisementRepository;

  @Override
  public Mono<Advertisement> getById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> advertisementRepository.findById(id))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(AD_NOT_EXIST, "Advertisement does not exist")));
  }

  @Override
  public Flux<Advertisement> getBySeller(Long sellerId) {
    if (Objects.isNull(sellerId) || sellerId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("sellerId"));
    }
    return asyncIterable(() -> advertisementRepository.getByAdOwner(sellerId));
  }

  @Override
  public Flux<Advertisement> getAll() {
    return asyncIterable(() -> advertisementRepository.getAll());
  }

  @Override
  public Mono<Advertisement> addNewAdvertisement(/*@Normal*/ Advertisement advertisement) {
    Date endDate = DateUtils.toDate(DateUtils.toLocalDate(advertisement.getEndDate()).plusDays(1));
    return async(() -> advertisementRepository.save(advertisement.setEndDate(endDate)));
  }

  @Override
  public Flux<Advertisement> getCurrentProductAd() {
    return asyncIterable(() -> advertisementRepository.getPresentProductAd());
  }

  @Override
  public Flux<Advertisement> getCurrentShopAd() {
    return asyncIterable(() -> advertisementRepository.getPresentShopAd());
  }
}
