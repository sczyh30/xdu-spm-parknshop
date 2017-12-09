package io.spm.parknshop.store.service;

import io.spm.parknshop.store.domain.Store;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface StoreService {

  Mono<Store> addStore(Store store);

  Mono<Store> modify(Long id, Store store);

  Mono<Optional<Store>> getById(Long id);

  Mono<Optional<Store>> getBySellerId(Long sellerId);

  Mono<Void> remove(Long id);

  Flux<Store> searchStoreByKeyword(String keyword);
}
