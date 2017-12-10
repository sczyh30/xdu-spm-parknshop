package io.spm.parknshop.api.controller;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.user.service.UserService;
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
  @Autowired
  private UserService userService;

  @PostMapping("/admin/add_admin")
  public Mono<Admin> apiAddAdmin(@RequestBody Admin admin) {
    return adminService.addAdmin(admin);
  }

  @PostMapping(value = "/admin/login")
  public Mono<?> apiAdminLogin(@RequestBody Admin admin) {
    return adminService.login(admin.getUsername(), admin.getPassword());
  }

  @PostMapping(value = "/admin/manage/user/{id}/set_blacklist")
  public Mono<?> apiSetUserBlacklist(@PathVariable("id") Long id) {
    return userService.setBlacklist(id);
  }

  @PostMapping(value = "/admin/manage/user/{id}/rm_blacklist")
  public Mono<?> apiRemoveUserBlacklist(@PathVariable("id") Long id) {
    return userService.removeFromBlacklist(id);
  }
}
