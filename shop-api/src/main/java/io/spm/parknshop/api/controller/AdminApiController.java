package io.spm.parknshop.api.controller;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.service.ApplyOperationService;
import io.spm.parknshop.seller.domain.StoreApplyDO;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.service.StoreService;
import io.spm.parknshop.user.domain.LoginVO;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.service.UserService;
import org.reactivestreams.Publisher;
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
  private StoreService storeService;
  @Autowired
  private UserService userService;
  @Autowired
  private SellerUserService sellerUserService;
  @Autowired
  private ApplyOperationService applyOperationService;

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
  public Mono<Double> getCommission() {
    return adminService.getCommission();
  }

  @GetMapping("/admin/shops/all")
  public Publisher<Store> apiGetAllShops() {
    return storeService.getAll();
  }

  @GetMapping("/admin/users/all")
  public Publisher<User> apiGetAllSellers() {
    return userService.getAllUsers();
  }

  @GetMapping("/admin/sellers/all")
  public Publisher<User> apiGetAllUsers() {
    return sellerUserService.getAllSellers();
  }

  @PostMapping("/admin/set_commission")
  public Mono<?> setCommission(@RequestBody String commission) {
    return adminService.setCommission(Double.valueOf(commission));
}

  @PostMapping("/admin/manage/apply/reject/{applyId}")
  public Mono<?> apiRejectAd(@PathVariable("applyId") Long applyId,
                             @RequestBody ApplyResult applyResult) {
    //TODO get processorId
    return applyOperationService.reject(applyId, "1", applyResult);
  }
}
