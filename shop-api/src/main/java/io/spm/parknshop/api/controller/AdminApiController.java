package io.spm.parknshop.api.controller;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * @author four
 */
@RestController
@RequestMapping("/api/v1/")
public class AdminApiController {

  @Autowired
  private AdminService adminService;

  @PostMapping(value = "/admin/login")
  public Mono<Boolean> apiAdminLogin(@RequestBody Admin admin) {
    return adminService.login(admin.getUsername(), admin.getPassword());
  }

}
