package io.spm.parknshop.user.service;

import io.spm.parknshop.user.domain.User;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Interface of user service.
 *
 * @author Eric Zhao
 */
public interface UserService {

  Mono<User> register(User user);

  Mono<Boolean> login(String username, String password);

  Mono<User> modifyDetail(Long id, User user);

  Mono<Optional<User>> getUserById(Long id);
}
