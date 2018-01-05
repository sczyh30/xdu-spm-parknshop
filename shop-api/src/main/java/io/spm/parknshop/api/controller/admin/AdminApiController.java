package io.spm.parknshop.api.controller.admin;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.configcenter.service.GlobalConfigService;
import io.spm.parknshop.query.service.StoreQueryService;
import io.spm.parknshop.query.vo.StoreVO;
import io.spm.parknshop.store.service.StoreService;
import io.spm.parknshop.user.domain.LoginVO;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
  private GlobalConfigService globalConfigService;

  @PostMapping("/admin/add_admin")
  public Mono<Admin> apiAddAdmin(@RequestBody Admin admin) {
    return adminService.addAdmin(admin);
  }

  @PostMapping(value = "/admin/login")
  public Mono<LoginVO> apiAdminLogin(@RequestBody Admin admin) {
    return adminService.login(admin.getUsername(), admin.getPassword());
  }

  @PostMapping(value = "/admin/manage/store/{id}/set_blacklist")
  public Mono<Long> apiSetStoreBlacklist(@PathVariable("id") Long id) {
    return storeService.setBlacklist(id);
  }

  @PostMapping(value = "/admin/manage/store/{id}/rm_blacklist")
  public Mono<Long> apiRemoveStoreBlacklist(@PathVariable("id") Long id) {
    return storeService.recoverFromBlacklist(id);
  }

  @GetMapping("/admin/commission")
  public Mono<Double> apiGetCommission() {
    return globalConfigService.getCommission();
  }

  @GetMapping("/admin/shops/all")
  public Publisher<StoreVO> apiGetAllShops() {
    return storeQueryService.getAll();
  }

  @PostMapping("/admin/set_commission")
  public Mono<?> apiSetCommission(@RequestParam("commission") Double commission) {
    return globalConfigService.setCommission(commission);
  }

}
