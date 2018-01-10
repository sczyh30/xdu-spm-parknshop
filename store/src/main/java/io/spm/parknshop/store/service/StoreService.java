package io.spm.parknshop.store.service;

import io.spm.parknshop.store.domain.Store;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface StoreService {

  Flux<Store> findAllNormalStore();

  Mono<Store> addStore(Store store);

  Mono<Store> modify(Long id, Store store);

  Mono<Store> getById(Long id);

  Mono<Optional<Store>> getBySellerId(Long sellerId);

  Mono<Long> remove(Long id);

  Flux<Store> searchStoreByKeyword(String keyword);

  Flux<Store> getAll();

  Mono<Long> setBlacklist(Long id);

  Mono<Long> recoverFromBlacklist(Long id);
}
