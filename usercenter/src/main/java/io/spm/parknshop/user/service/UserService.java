package io.spm.parknshop.user.service;

import io.spm.parknshop.user.domain.LoginVO;
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

  Mono<LoginVO> login(String username, String password);

  Mono<User> modifyDetail(Long id, User user);

  Mono<Long> modifyPassword(Long id, PrincipalModifyDO user);

  Mono<Optional<User>> getUserById(Long id);

  Flux<User> searchCustomerByKeyword(String keyword);

  Mono<Long> setBlacklist(Long id);

  Mono<Long> removeFromBlacklist(Long id);

  Mono<Long> deleteUser(Long id);

  Flux<User> getAllUsers();
}
