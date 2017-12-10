package io.spm.parknshop.admin.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.seller.domain.StoreApplyDO;
import io.spm.parknshop.store.domain.Store;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface of admin Service
 */
public interface AdminService {

  Mono<String> login(String username, String password);

  Mono<Admin> modifyDetail(Long id, Admin admin);

  Mono<Admin> addAdmin(Admin admin);

  Flux<StoreApplyDO> getApplyList();

  Mono<Store> approveApply(Long id);

  Mono<Long> rejectApply(Long id);

  Mono<Boolean> setCommission(Double commission);

  Mono<Double> getCommission();

}
