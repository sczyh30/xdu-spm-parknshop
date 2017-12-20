package io.spm.parknshop.delivery.service.impl;

import io.spm.parknshop.delivery.domain.DeliveryAddress;
import io.spm.parknshop.delivery.service.DeliveryAddressService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

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
    return null;
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
