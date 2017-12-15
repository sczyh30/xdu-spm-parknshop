package io.spm.parknshop.seller.service.impl;

import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.seller.domain.StoreApplyDO;
import io.spm.parknshop.seller.repository.StoreApplyRepository;
import io.spm.parknshop.seller.service.SellerService;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * Biz service implementation of {@link SellerService}.
 *
 * @author Eric Zhao
 * @author four
 */
@Service
public class SellerServiceImpl implements SellerService {

  @Autowired
  private StoreApplyRepository storeApplyRepository;

  @Autowired
  private StoreService storeService;

  @Override
  public Mono<String> applyStore(Long sellerId, Store store) {
    return checkApplyParams(sellerId, store)
        .flatMap(v -> checkPendingApply(sellerId))
        .switchIfEmpty(checkIfOwnedStore(sellerId))
        .map(Object::toString)
        .switchIfEmpty(Mono.just(convert(store))
            .flatMap(e -> storeApplyRepository.putStoreApply(sellerId, e)
                .map(v -> e.getId())
            )
        );
  }

  private Mono<Long> checkApplyParams(Long sellerId, Store store) {
    if (Objects.isNull(sellerId) || sellerId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("sellerId"));
    }
    return Mono.just(sellerId);
  }

  private Mono<StoreApplyDO> checkPendingApply(Long sellerId) {
    return storeApplyRepository.getPendingStoreBySellerId(sellerId)
        .flatMap(e -> Mono.error(new ServiceException(STORE_APPLY_IN_PROGRESS, "You already have a store apply in progress")));
  }

  private /*Mono<?>*/ Mono<StoreApplyDO> checkIfOwnedStore(Long sellerId) {
    return storeService.getBySellerId(sellerId)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .flatMap(e -> Mono.error(new ServiceException(STORE_ALREADY_OPEN, "You already have a store")));
  }

  private StoreApplyDO convert(Store store) {
    return new StoreApplyDO()
        .setId(UUID.randomUUID().toString())
        .setApplyTime(new Date())
        .setStore(store);
  }
}
