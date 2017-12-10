package io.spm.parknshop.user.service;

import io.spm.parknshop.common.auth.AuthCenter;
import io.spm.parknshop.common.auth.JWTUtils;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.user.domain.PrincipalModifyDO;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.domain.UserStatus;
import io.spm.parknshop.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Mono<User> register(User user) {
    if (!isValidUser(user)) {
      return Mono.error(ExceptionUtils.invalidParam("user"));
    }
    if (Objects.nonNull(user.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("user ID should not be provided"));
    }
    return checkDuplicateInfo(user)
      .switchIfEmpty(async(() -> userRepository.save(encrypt(user))));
  }

  private User encrypt(/*@NonNull*/ User user) {
    return user.setPassword(AuthCenter.encryptDefault(/*@NonNull*/ user.getPassword()));
  }

  private Mono<User> checkDuplicateInfo(/*@NonNull*/ User user) {
    return async(() -> userRepository.getIdByUsername(user.getUsername()))
      .flatMap(e -> Mono.<User>error(new ServiceException(USER_ALREADY_EXISTS, "User already exists: " + user.getUsername())))
      .switchIfEmpty(async(() -> userRepository.getIdByEmail(user.getEmail())).map(e -> new User()))
      .flatMap(e -> Mono.<User>error(new ServiceException(USER_INFO_DUPLICATE, "User info duplicate: " + user.getEmail())))
      .switchIfEmpty(async(() -> userRepository.getIdByTelephone(user.getTelephone())).map(e -> new User()))
      .flatMap(e -> Mono.<User>error(new ServiceException(USER_INFO_DUPLICATE, "User info duplicate: " + user.getTelephone())));
  }

  @Override
  public Mono<String> login(final String username, final String password) {
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return Mono.error(ExceptionUtils.invalidParam("username/password"));
    }
    return async(() -> userRepository.getByUsername(username))
      .flatMap(this::extractUser)
      .flatMap(user ->
        Mono.just(verifyCredential(user, username, password))
          .flatMap(ok -> generateToken(user, ok)))
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

  @Override
  public Mono<User> modifyDetail(Long id, User user) {
    // TODO: Exclude username and password modify!
    if (!isValidUser(user)) {
      return Mono.error(ExceptionUtils.invalidParam("user"));
    }
    if (Objects.nonNull(user.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("user ID should not be provided"));
    }
    if (!id.equals(user.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> userRepository.save(user.setGmtModified(new Date())));
  }

  @Override
  public Mono<Void> modifyPassword(Long id, PrincipalModifyDO user) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    if (Objects.isNull(user) || Objects.isNull(user.getId()) || Objects.isNull(user.getNewPassword()) || Objects.isNull(user.getOldPassword())) {
      return Mono.error(ExceptionUtils.invalidParam("user"));
    }
    if (!id.equals(user.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> userRepository.findById(id))
      .flatMap(e -> e.map(Mono::just).orElseGet(() -> Mono.error(new ServiceException(USER_NOT_EXIST, "User does not exist"))))
      .map(u -> verifyCredential(u, u.getUsername(), user.getOldPassword()))
      .flatMap(ok -> {
        if (ok) {
          return asyncExecute(() -> userRepository.modifyPassword(AuthCenter.encryptDefault(user.getNewPassword()), user.getId()));
        } else {
          return Mono.error(new ServiceException(USER_MODIFY_OLD_PASSWORD_NOT_MATCH, "Incorrect old password"));
        }
      });
  }

  @Override
  public Mono<Optional<User>> getUserById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> userRepository.findById(id));
  }

  @Override
  public Flux<User> searchUserByKeyword(String keyword) {
    if (StringUtils.isEmpty(keyword)) {
      return Flux.error(ExceptionUtils.invalidParam("keyword"));
    }
    return asyncIterable(() -> userRepository.searchUserByKeyword(keyword));
  }

  @Override
  public Mono<Void> setBlacklist(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> userRepository.modifyStatus(UserStatus.BLACKLIST, id));
  }

  @Override
  public Mono<Void> removeFromBlacklist(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> userRepository.modifyStatus(UserStatus.NORMAL, id));
  }

  @Override
  public Mono<Long> deleteUser(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    userRepository.deleteById(id);
    return Mono.just(0L);
  }

  private boolean isValidUser(User user) {
    return Objects.nonNull(user) && Objects.nonNull(user.getUsername()) && Objects.nonNull(user.getPassword());
  }
}
