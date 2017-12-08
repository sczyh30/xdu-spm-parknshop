package io.spm.parknshop.admin.service;

import io.spm.parknshop.admin.domain.Admin;
import reactor.core.publisher.Mono;

/**
 * Interface of admin Service
 */
public interface AdminService {

  Mono<Boolean> login(String username, String password);

  Mono<Admin> modifyDetail(String id, Admin admin);

  Mono<Admin> addAdmin(Admin admin);


}
