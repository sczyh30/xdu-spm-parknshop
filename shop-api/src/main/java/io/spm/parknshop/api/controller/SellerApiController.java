package io.spm.parknshop.api.controller;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.seller.service.SellerService;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.user.domain.LoginVO;
import io.spm.parknshop.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class SellerApiController {

  @Autowired
  private SellerService sellerService;
  @Autowired
  private SellerUserService sellerUserService;

  @PostMapping("/seller/apply/apply_store")
  public /*Mono<String>*/ Mono<?> apiApplyStore(@RequestBody Store store) {
    if (store == null || store.getSellerId() == null) {
      return Mono.error(ExceptionUtils.invalidParam("store"));
    }
    return sellerService.applyStore(store.getSellerId(), store);
  }

  @GetMapping("/seller/u/{id}")
  public Mono<User> apiGetSellerById(@PathVariable("id") Long id) {
    return sellerUserService.getSellerById(id)
      .filter(Optional::isPresent)
      .map(Optional::get);
  }

  @PostMapping("/seller/register")
  public Mono<User> apiSellerRegister(@RequestBody User user) {
    return sellerUserService.register(user);
  }

  @PostMapping("/seller/login")
  public Mono<LoginVO> apiSellerLogin(@RequestBody User user) {
    return sellerUserService.login(user.getUsername(), user.getPassword());
  }

}
