package io.spm.parknshop.seller.service.impl;

import io.spm.parknshop.common.auth.AuthCenter;
import io.spm.parknshop.common.auth.AuthRoles;
import io.spm.parknshop.common.auth.JWTUtils;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.user.domain.LoginVO;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.repository.UserRepository;
import io.spm.parknshop.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * @author Eric Zhao
 */
@Service
public class SellerUserServiceImpl implements SellerUserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Override
  public Flux<User> searchSellerByKeyword(String keyword) {
    if (StringUtils.isEmpty(keyword)) {
      return Flux.error(ExceptionUtils.invalidParam("keyword"));
    }
    return asyncIterable(() -> userRepository.searchSellerByKeyword(keyword));
  }

  @Override
  public Flux<User> getAllSellers() {
    return asyncIterable(() -> userRepository.getAllByUserType(AuthRoles.SELLER));
  }

  @Override
  public Mono<Optional<User>> getSellerById(Long sellerId) {
    if (Objects.isNull(sellerId) || sellerId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> userRepository.getSellerById(sellerId));
  }

  @Override
  public Mono<User> register(User user) {
    return userService.register(user.setUserType(AuthRoles.SELLER));
  }

  @Override
  public Mono<LoginVO> login(final String username, final String password) {
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return Mono.error(ExceptionUtils.invalidParam("username/password"));
    }
    return async(() -> userRepository.getSellerByUsername(username))
      .flatMap(this::extractUser)
      .flatMap(user ->
        Mono.just(verifyCredential(user, username, password))
          .flatMap(ok -> generateToken(user, ok))
          .map(token -> new LoginVO(token, username, user.getId())))
      .switchIfEmpty(Mono.error(ExceptionUtils.loginIncorrect()));
  }

  private Mono<User> extractUser(Optional<User> userOpt) {
    return userOpt.map(Mono::just)
      .orElseGet(() -> Mono.error(ExceptionUtils.loginIncorrect()));
  }

  private boolean verifyCredential(/*@NonNull*/ User user, /*@NonNull*/ String username, /*@NonNull*/ String password) {
    return username.equals(user.getUsername()) && AuthCenter.decryptMatches(password, user.getPassword());
  }

  private Mono<String> generateToken(/*@NonNull*/ User user, boolean ok) {
    if (ok) {
      return JWTUtils.generateToken(user.getUsername(), user.getId(), user.getUserType());
    } else {
      return Mono.error(ExceptionUtils.loginIncorrect());
    }
  }

}
