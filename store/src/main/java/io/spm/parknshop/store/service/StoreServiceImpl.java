package io.spm.parknshop.store.service;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.delivery.domain.DeliveryTemplate;
import io.spm.parknshop.delivery.repository.DeliveryTemplateRepository;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.domain.StoreStatus;
import io.spm.parknshop.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class StoreServiceImpl implements StoreService {

  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private DeliveryTemplateRepository deliveryTemplateRepository;

  @Override
  public Mono<Store> addStore(Store store) {
    if (!isValidNewStore(store)) {
      return Mono.error(ExceptionUtils.invalidParam("store"));
    }
    if (Objects.nonNull(store.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("storeId should not be provided"));
    }
    //TODO Seller may no exist
    return async(() -> createInitialStore(store));
  }

  private Store createInitialStore(Store store) {
    Store newStore = storeRepository.save(store);
    DeliveryTemplate deliveryTemplate = new DeliveryTemplate().setStoreId(store.getId())
      .setExpressType(1).setDescription("Express").setDefaultPrice(0.0d);
    deliveryTemplateRepository.save(deliveryTemplate);
    return newStore;
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
    //TODO: modify status security problem!
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
  public Mono<Long> remove(Long id) {
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

  @Override
  public Flux<Store> getAll() {
    return asyncIterable(() -> storeRepository.findAll());
  }

  @Override
  public Mono<Long> setBlacklist(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> storeRepository.modifyStatus(StoreStatus.BLACK_LIST, id));
  }

  @Override
  public Mono<Long> recoverFromBlacklist(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> storeRepository.modifyStatus(StoreStatus.NORMAL, id));
  }

  private static boolean isValidNewStore(Store store){
    return Optional.ofNullable(store)
        .map(e -> store.getBriefDescription())
        .map(e -> store.getSellerId())
        .map(e -> store.getName())
        .map(e -> store.getTelephone())
        .isPresent();
  }

  private  static boolean isValidStore(Store store){
    return Optional.ofNullable(store)
        .map(e -> store.getId())
        .map(e -> store.getBriefDescription())
        .map(e -> store.getSellerId())
        .map(e -> store.getName())
        .map(e -> store.getTelephone())
        .isPresent();
  }
}
