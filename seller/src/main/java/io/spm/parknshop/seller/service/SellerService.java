package io.spm.parknshop.seller.service;

import io.spm.parknshop.store.domain.StoreDTO;
import reactor.core.publisher.Mono;

/**
 * Interface of seller biz service.
 */
public interface SellerService {

  Mono<Long> applyStore(String sellerProposer, StoreDTO storeDTO);

}
