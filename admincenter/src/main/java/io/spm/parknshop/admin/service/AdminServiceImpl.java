package io.spm.parknshop.admin.service;


import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.repository.AdminRepository;
import io.spm.parknshop.admin.repository.CommissionRepository;
import io.spm.parknshop.common.auth.AuthCenter;
import io.spm.parknshop.common.auth.AuthRoles;
import io.spm.parknshop.common.auth.JWTUtils;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.seller.domain.StoreApplyDO;
import io.spm.parknshop.seller.repository.StoreApplyRepository;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.service.StoreService;
import io.spm.parknshop.user.domain.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class AdminServiceImpl implements AdminService {

  @Autowired
  private AdminRepository adminRepository;
  @Autowired
  private StoreApplyRepository storeApplyRepository;
  @Autowired
  private StoreService storeService;
  @Autowired
  private CommissionRepository commissionRepository;

  @Override
  public Mono<LoginVO> login(String username, String password) {
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return Mono.error(ExceptionUtils.invalidParam("username/password"));
    }
    return async(() -> adminRepository.getByUsername(username))
      .flatMap(this::extractAdmin)
      .flatMap(admin ->
        Mono.just(verifyCredential(admin, username, password))
          .flatMap(ok -> generateToken(admin, ok))
          .map(token -> new LoginVO(token, username, admin.getId())))
      .switchIfEmpty(Mono.error(ExceptionUtils.loginIncorrect()));
  }

  private Mono<Admin> extractAdmin(Optional<Admin> adminOpt) {
    return adminOpt.map(Mono::just)
      .orElseGet(() -> Mono.error(ExceptionUtils.loginIncorrect()));
  }

  @Override
  public Mono<Admin> modifyDetail(Long id, Admin admin) {
    if (!isValidUser(admin)) {
      return Mono.error(ExceptionUtils.invalidParam("admin"));
    }
    if (Objects.nonNull(admin.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("admin ID should not be provided"));
    }
    if (!id.equals(admin.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> adminRepository.save(admin));
  }

  @Override
  public Mono<Admin> addAdmin(Admin admin) {
    if (!isValidUser(admin)) {
      return Mono.error(ExceptionUtils.invalidParam("admin"));
    }
    if (Objects.nonNull(admin.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("admin ID should not be provided"));
    }
    return getIdByUsername(admin.getUsername())
        .flatMap(e -> Mono.<Admin>error(new ServiceException(USER_ALREADY_EXISTS, "Admin already exists: " + admin.getUsername())))
        .switchIfEmpty(async(() -> adminRepository.save(encrypt(admin))));
  }

  private Admin encrypt(/*@NonNull*/ Admin admin) {
    return admin.setPassword(AuthCenter.encryptDefault(/*@NonNull*/ admin.getPassword()));
  }

  private Mono<String> generateToken(/*@NonNull*/ Admin admin, boolean ok) {
    if (ok) {
      return JWTUtils.generateToken(admin.getUsername(), admin.getId(), AuthRoles.ADMIN);
    } else {
      return Mono.error(ExceptionUtils.loginIncorrect());
    }
  }
  @Override
  public Flux<StoreApplyDO> getApplyList() {
    return storeApplyRepository.getApplyStoreList();
  }

  @Override
  public Mono<Store> approveApply(Long id) {
    return storeApplyRepository.getPendingStoreBySellerId(id)
        .flatMap(e ->approveOne(e.getStore()))
        .switchIfEmpty(Mono.error(new ServiceException(STORE_APPLY_NOT_EXIST,"no apply record")));
  }

  @Override
  public Mono<Long> rejectApply(Long id) {
    return storeApplyRepository.getPendingStoreBySellerId(id)
        .flatMap(e -> storeApplyRepository.deleteOneApply(id))
        .switchIfEmpty(Mono.error(new ServiceException(STORE_APPLY_NOT_EXIST,"no apply record")));
  }

  @Override
  public Mono<Boolean> setCommission(Double commission) {
    if (commission >= 100 || commission <= 0) {
     return Mono.error(new ServiceException(COMMISSION_IS_ERROR, "commission is error"));
    }
    return commissionRepository.setCommission(commission);
  }

  @Override
  public Mono<Double> getCommission() {
    return commissionRepository.getCommission();
  }


  private Mono<Store> approveOne(Store store) {
    return storeApplyRepository.deleteOneApply(store.getSellerId())
        .flatMap(e -> storeService.addStore(store))
        .switchIfEmpty(Mono.error(new ServiceException(STORE_ALREADY_OPEN,"this user already have a store")));
  }

  private Mono<String> getIdByUsername(String username) {
    if (StringUtils.isEmpty(username)) {
      return Mono.error(ExceptionUtils.invalidParam("username"));
    }
    return async(() -> adminRepository.getIdByUsername(username));
  }


  private boolean verifyCredential(/*@NonNull*/ Admin admin, /*@NonNull*/ String username, /*@NonNull*/ String password) {
    return username.equals(admin.getUsername()) && AuthCenter.decryptMatches(password, admin.getPassword());
  }

  private boolean isValidUser(Admin admin) {
    return Objects.nonNull(admin) && Objects.nonNull(admin.getUsername()) && Objects.nonNull(admin.getPassword());
  }
}
