package io.spm.parknshop.api.controller.admin;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.admin.service.DBBackupService;
import io.spm.parknshop.configcenter.service.GlobalConfigService;
import io.spm.parknshop.query.service.StoreQueryService;
import io.spm.parknshop.query.vo.StoreVO;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.store.service.StoreService;
import io.spm.parknshop.user.domain.LoginVO;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.service.UserService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author four
 * @author Eric Zhao
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
  private DBBackupService dbBackupService;
  @Autowired
  private GlobalConfigService globalConfigService;

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

  @DeleteMapping("/admin/manage/user/{id}")
  public Mono<Long> apiDeleteUser(@PathVariable("id") Long id) {
    return userService.deleteUser(id);
  }

  @GetMapping("/admin/commission")
  public Mono<Double> apiGetCommission() {
    return globalConfigService.getCommission();
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
    return globalConfigService.setCommission(commission);
  }

  @GetMapping("/admin/db/backups")
  public Flux<String> apiGetBackups() {
    return dbBackupService.getBackups();
  }
  @PostMapping("/admin/db/recover")
  public Mono<String> apiRecover(@RequestParam("fileName") String fileName) {
    return dbBackupService.recover(fileName);
  }
  @GetMapping("/admin/db/backup")
  public Mono<String> apiBackup() {
    return dbBackupService.backupDB();
  }
}
