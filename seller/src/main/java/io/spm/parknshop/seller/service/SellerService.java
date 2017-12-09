package io.spm.parknshop.seller.service;

import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.user.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface of seller service
 *
 */
public interface SellerService {

  Flux<User> searchSellerByKeyword(String keyword);

  Mono<Boolean> applyStore(Store store);
}
