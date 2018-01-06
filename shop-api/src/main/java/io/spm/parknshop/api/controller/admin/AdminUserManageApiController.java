package io.spm.parknshop.api.controller.admin;

import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.service.UserService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class AdminUserManageApiController {

  @Autowired
  private AdminService adminService;
  @Autowired
  private UserService userService;
  @Autowired
  private SellerUserService sellerUserService;

  @PostMapping(value = "/admin/manage/user/{id}/set_blacklist")
  public Mono<Long> apiSetUserBlacklist(@PathVariable("id") Long id) {
    return userService.setBlacklist(id);
  }

  @PostMapping(value = "/admin/manage/user/{id}/rm_blacklist")
  public Mono<Long> apiRemoveUserBlacklist(@PathVariable("id") Long id) {
    return userService.removeFromBlacklist(id);
  }

  @DeleteMapping("/admin/manage/customer/{id}")
  public Mono<Long> apiDeleteCustomer(@PathVariable("id") Long id) {
    return userService.deleteUser(id);
  }

  @DeleteMapping("/admin/manage/seller/{id}")
  public Mono<Long> apiDeleteSeller(@PathVariable("id") Long sellerId) {
    return sellerUserService.deleteSeller(sellerId);
  }

  @GetMapping("/admin/sellers/all")
  public Publisher<User> apiGetAllSellers() {
    return sellerUserService.getAllSellers();
  }

  @GetMapping("/admin/customers/all")
  public Publisher<User> apiGetAllCustomers() {
    return userService.getAllCustomers();
  }

  @GetMapping("/admin/users/all")
  public Publisher<User> apiGetAllUsers() {
    return userService.getAllUsers();
  }

}
