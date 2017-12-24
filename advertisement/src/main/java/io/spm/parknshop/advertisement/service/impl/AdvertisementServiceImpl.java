package io.spm.parknshop.advertisement.service.impl;

import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.repository.AdvertisementRepository;
import io.spm.parknshop.advertisement.service.AdvertisementService;
import io.spm.parknshop.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

  @Autowired
  private AdvertisementRepository advertisementRepository;

  @Override
  public Mono<Advertisement> getById(Long id) {
    // TODO: Check id.
    return async(() -> advertisementRepository.findById(id))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(AD_UNKNOWN_TYPE, "Advertisement not found")));
  }

  @Override
  public Mono<Advertisement> addNewAdvertisement(Advertisement advertisement) {
    return async(() -> advertisementRepository.save(advertisement));
  }
}
