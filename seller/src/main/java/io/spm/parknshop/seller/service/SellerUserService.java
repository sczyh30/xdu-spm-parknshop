package io.spm.parknshop.seller.service;

import io.spm.parknshop.user.domain.LoginVO;
import io.spm.parknshop.user.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Interface of seller user service.
 *
 * @author Eric Zhao
 */
public interface SellerUserService {

  Flux<User> searchSellerByKeyword(String keyword);

  Flux<User> getAllSellers();

  Mono<LoginVO> login(String username, String password);
}