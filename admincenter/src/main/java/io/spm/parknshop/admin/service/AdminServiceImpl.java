package io.spm.parknshop.admin.service;


import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.repository.AdminRepostitoy;
import io.spm.parknshop.common.auth.AuthCenter;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;
import static io.spm.parknshop.common.exception.ErrorConstants.USER_ALREADY_EXISTS;

@Service
public class AdminServiceImpl implements AdminService {

  @Autowired
  private AdminRepostitoy adminRepostitoy;

  @Override
  public Mono<Boolean> login(String username, String password) {
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return Mono.error(ExceptionUtils.invalidParam("username/password"));
    }
    return async(() -> adminRepostitoy.getByUsername(username))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(e -> verifyCredential(e, username, password))
        .switchIfEmpty(Mono.just(false));
  }

  @Override
  public Mono<Admin> modifyDetail(String id, Admin admin) {
    if (!isValidUser(admin)) {
      return Mono.error(ExceptionUtils.invalidParam("user"));
    }
    if (Objects.nonNull(admin.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("user ID should not be provided"));
    }
    if (!id.equals(admin.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> adminRepostitoy.save(admin));
  }

  @Override
  public Mono<Admin> addAdmin(Admin admin) {
    if (!isValidUser(admin)) {
      return Mono.error(ExceptionUtils.invalidParam("user"));
    }
    if (Objects.nonNull(admin.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("user ID should not be provided"));
    }
    return getIdByUsername(admin.getUsername())
        .flatMap(e -> Mono.<Admin>error(new ServiceException(USER_ALREADY_EXISTS, "User already exists: " + admin.getUsername())))
        .switchIfEmpty(async(() -> adminRepostitoy.save(admin)));
  }

  private Mono<String> getIdByUsername(String username) {
    if (StringUtils.isEmpty(username)) {
      return Mono.error(ExceptionUtils.invalidParam("username"));
    }
    return async(() -> adminRepostitoy.getIdByUsername(username));
  }


  private boolean verifyCredential(/*@NonNull*/ Admin admin, /*@NonNull*/ String username, /*@NonNull*/ String password) {
    return username.equals(admin.getUsername()) && AuthCenter.encryptDefault(password).equals(admin.getPassword());
  }

  private boolean isValidUser(Admin admin) {
    return Objects.nonNull(admin) && Objects.nonNull(admin.getUsername()) && Objects.nonNull(admin.getPassword());
  }
}
