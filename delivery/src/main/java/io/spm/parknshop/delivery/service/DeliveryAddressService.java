package io.spm.parknshop.delivery.service;

import io.spm.parknshop.delivery.domain.DeliveryAddress;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface DeliveryAddressService {

  Mono<DeliveryAddress> addAddress(DeliveryAddress address);

  Mono<DeliveryAddress> updateAddress(Long id, DeliveryAddress address);

  Mono<Optional<DeliveryAddress>> getById(Long id);

  Flux<DeliveryAddress> getByUserId(Long userId);

  Mono<Long> deleteAddress(Long id);
}
