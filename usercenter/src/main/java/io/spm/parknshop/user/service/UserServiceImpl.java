package io.spm.parknshop.user.service;

import io.spm.parknshop.common.auth.AuthCenter;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;
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
        .switchIfEmpty(async(() -> userRepository.save(user)));
  }

  private Mono<User> checkDuplicateInfo(/*@NonNull*/ User user) {
    return async(() -> userRepository.getIdByUsername(user.getUsername()))
        .flatMap(e -> Mono.<User>error(new ServiceException(USER_ALREADY_EXISTS, "User already exists: " + user.getUsername())))
        .switchIfEmpty(async(() -> userRepository.getIdByEmail(user.getEmail())).map(e -> new User()))
        .flatMap(e -> Mono.<User>error(new ServiceException(USER_INFO_DUPLICATE, "User info duplicate: " + user.getEmail())))
        .switchIfEmpty(async(() -> userRepository.getIdByTelephone(user.getTelephone())).map(e -> new User()))
        .flatMap(e -> Mono.<User>error(new ServiceException(USER_INFO_DUPLICATE, "User info duplicate: " + user.getTelephone())));
  }

  private Mono<Long> getIdByUsername(String username) {
    if (StringUtils.isEmpty(username)) {
      return Mono.error(ExceptionUtils.invalidParam("username"));
    }
    return async(() -> userRepository.getIdByUsername(username));
  }

  @Override
  public Mono<Boolean> login(final String username, final String password) {
    if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
      return Mono.error(ExceptionUtils.invalidParam("username/password"));
    }
    return async(() -> userRepository.getByUsername(username))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(e -> verifyCredential(e, username, password))
        .switchIfEmpty(Mono.just(false));
  }

  private boolean verifyCredential(/*@NonNull*/ User user, /*@NonNull*/ String username, /*@NonNull*/ String password) {
    return username.equals(user.getUsername()) && AuthCenter.encryptDefault(password).equals(user.getPassword());
  }

  @Override
  public Mono<User> modifyDetail(Long id, User user) {
    if (!isValidUser(user)) {
      return Mono.error(ExceptionUtils.invalidParam("user"));
    }
    if (Objects.nonNull(user.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("user ID should not be provided"));
    }
    if (!id.equals(user.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> userRepository.save(user));
  }

  @Override
  public Mono<Optional<User>> getUserById(Long id) {
    if (!Objects.nonNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> userRepository.findById(id));
  }

  private boolean isValidUser(User user) {
    return Objects.nonNull(user) && Objects.nonNull(user.getUsername()) && Objects.nonNull(user.getPassword());
  }
}
