package io.spm.parknshop.store.service;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.asyncExecute;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.asyncIterable;

@Service
public class StoreServiceImpl implements StoreService {

  @Autowired
  private StoreRepository storeRepository;

  @Override
  public Mono<Store> addStore(Store store) {
    if (!isValidNewStore(store)) {
      return Mono.error(ExceptionUtils.invalidParam("store"));
    }
    if (Objects.nonNull(store.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("storeId should not be provided"));
    }
    //TODO Seller may no exist
    return async(() -> storeRepository.save(store));
  }

  @Override
  public Mono<Store> modify(Long id, Store store) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("storeId"));
    }
    if (!isValidStore(store)) {
      return Mono.error(ExceptionUtils.invalidParam("store"));
    }
    if (!id.equals(store.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> storeRepository.save(store.setGmtModified(new Date())));
  }

  @Override
  public Mono<Optional<Store>> getById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> storeRepository.findById(id));
  }

  @Override
  public Mono<Optional<Store>> getBySellerId(Long sellerId) {
    if (Objects.isNull(sellerId) || sellerId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("sellerId"));
    }

    return async(() -> storeRepository.getBySellerId(sellerId));
  }

  @Override
  public Mono<Void> remove(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> storeRepository.deleteById(id));
  }

  @Override
  public Flux<Store> searchStoreByKeyword(String keyword) {
    if(Objects.isNull(keyword) || "".equals(keyword)) {
      return Flux.error(ExceptionUtils.invalidParam("keyword"));
    }
    return asyncIterable(() -> storeRepository.searchStoreByKeyword(keyword));
  }

  private static boolean isValidNewStore(Store store){
    return Optional.ofNullable(store)
        .map(e -> store.getEmail())
        .map(e -> store.getSellerId())
        .map(e -> store.getName())
        .map(e -> store.getTelephone())
        .isPresent();
  }

  private  static boolean isValidStore(Store store){
    return Optional.ofNullable(store)
        .map(e -> store.getId())
        .map(e -> store.getEmail())
        .map(e -> store.getSellerId())
        .map(e -> store.getName())
        .map(e -> store.getTelephone())
        .isPresent();
  }
}
