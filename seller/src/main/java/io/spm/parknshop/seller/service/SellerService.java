package io.spm.parknshop.seller.service;

import io.spm.parknshop.store.domain.Store;
import reactor.core.publisher.Mono;

/**
 * Interface of seller biz service.
 */
public interface SellerService {

  Mono<String> applyStore(Long userId, Store store);

}
