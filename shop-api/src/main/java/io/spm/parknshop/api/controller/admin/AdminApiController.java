package io.spm.parknshop.api.controller.admin;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.service.ApplyProcessService;
import io.spm.parknshop.query.service.StoreQueryService;
import io.spm.parknshop.query.vo.StoreVO;
import io.spm.parknshop.seller.domain.StoreApplyDO;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.service.StoreService;
import io.spm.parknshop.user.domain.LoginVO;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.service.UserService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
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
  private StoreService storeService;
  @Autowired
  private StoreQueryService storeQueryService;
  @Autowired
  private UserService userService;
  @Autowired
  private SellerUserService sellerUserService;

  @Autowired
  @Qualifier("adApplyService")
  private ApplyProcessService applyProcessService;

  @PostMapping("/admin/add_admin")
  public Mono<Admin> apiAddAdmin(@RequestBody Admin admin) {
    return adminService.addAdmin(admin);
  }

  @PostMapping(value = "/admin/login")
  public Mono<LoginVO> apiAdminLogin(@RequestBody Admin admin) {
    return adminService.login(admin.getUsername(), admin.getPassword());
  }

  @PostMapping(value = "/admin/manage/user/{id}/set_blacklist")
  public Mono<Long> apiSetUserBlacklist(@PathVariable("id") Long id) {
    return userService.setBlacklist(id);
  }

  @PostMapping(value = "/admin/manage/user/{id}/rm_blacklist")
  public Mono<Long> apiRemoveUserBlacklist(@PathVariable("id") Long id) {
    return userService.removeFromBlacklist(id);
  }

  @PostMapping(value = "/admin/manage/store/{id}/set_blacklist")
  public Mono<Long> apiSetStoreBlacklist(@PathVariable("id") Long id) {
    return storeService.setBlacklist(id);
  }

  @PostMapping(value = "/admin/manage/store/{id}/rm_blacklist")
  public Mono<Long> apiRemoveStoreBlacklist(@PathVariable("id") Long id) {
    return storeService.recoverFromBlacklist(id);
  }

  @GetMapping("/admin/apply_list")
  public Publisher<StoreApplyDO> apiGetApplyList() {
    return adminService.getApplyList();
  }

  @PostMapping("/admin/approve_apply/{sellerId}")
  public Mono<Store> apiApproveApply(@PathVariable("sellerId") Long sellerId) {
    return adminService.approveApply(sellerId);
  }

  @PostMapping("/admin/reject_apply/{sellerId}")
  public Mono<Long> apiRejectApply(@PathVariable("sellerId") Long sellerId) {
    return adminService.rejectApply(sellerId);
  }

  @DeleteMapping("/admin/manage/user/{id}")
  public Mono<Long> apiDeleteUser(@PathVariable("id") Long id) {
    return userService.deleteUser(id);
  }

  @GetMapping("/admin/commission")
  public Mono<Double> apiGetCommission() {
    return adminService.getCommission();
  }

  @GetMapping("/admin/shops/all")
  public Publisher<StoreVO> apiGetAllShops() {
    return storeQueryService.getAll();
  }

  @GetMapping("/admin/users/all")
  public Publisher<User> apiGetAllSellers() {
    return userService.getAllUsers();
  }

  @GetMapping("/admin/customers/all")
  public Publisher<User> apiGetAllCustomers() {
    return userService.getAllCustomers();
  }

  @GetMapping("/admin/sellers/all")
  public Publisher<User> apiGetAllUsers() {
    return sellerUserService.getAllSellers();
  }

  @PostMapping("/admin/set_commission")
  public Mono<?> apiSetCommission(@RequestParam("commission") Double commission) {
    return adminService.setCommission(commission);
  }
}
