package io.spm.parknshop.user.service;

import io.spm.parknshop.user.domain.PrincipalModifyDO;
import io.spm.parknshop.user.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Interface of user service.
 *
 * @author Eric Zhao
 */
public interface UserService {

  Mono<User> register(User user);

  Mono<String> login(String username, String password);

  Mono<User> modifyDetail(Long id, User user);

  Mono<Void> modifyPassword(Long id, PrincipalModifyDO user);

  Mono<Optional<User>> getUserById(Long id);

  Flux<User> searchUserByKeyword(String keyword);

  Mono<Void> setBlacklist(Long id);

  Mono<Void> removeFromBlacklist(Long id);

  Mono<Long> deleteUser(Long id);
}
