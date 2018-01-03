package io.spm.parknshop.admin.service;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.user.domain.LoginVO;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Interface of admin Service
 */
public interface AdminService {

  Mono<LoginVO> login(String username, String password);

  Mono<Admin> modifyProfileDetail(Long id, Admin admin);

  Mono<Admin> addAdmin(Admin admin);

  Mono<Optional<Admin>> getById(Long id);

}
