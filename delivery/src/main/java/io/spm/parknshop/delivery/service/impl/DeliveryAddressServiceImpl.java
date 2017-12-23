package io.spm.parknshop.delivery.service.impl;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.delivery.domain.DeliveryAddress;
import io.spm.parknshop.delivery.repository.DeliveryAddressRepository;
import io.spm.parknshop.delivery.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

  @Autowired
  private DeliveryAddressRepository deliveryAddressRepository;

  @Override
  public Mono<DeliveryAddress> addAddress(DeliveryAddress address) {
    return null;
  }

  @Override
  public Mono<DeliveryAddress> updateAddress(Long id, DeliveryAddress address) {
    return null;
  }

  @Override
  public Mono<Optional<DeliveryAddress>> getById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> deliveryAddressRepository.findById(id));
  }

  @Override
  public Flux<DeliveryAddress> getByUserId(Long userId) {
    return null;
  }

  @Override
  public Mono<Long> deleteAddress(Long id) {
    return null;
  }
}
