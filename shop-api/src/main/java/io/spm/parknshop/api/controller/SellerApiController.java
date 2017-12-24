package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.seller.service.SellerService;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.user.domain.LoginVO;
import io.spm.parknshop.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
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
  public /*Mono<String>*/ Mono<?> apiApplyStore(ServerWebExchange exchange, @RequestBody Store store) {
    return AuthUtils.getUserId(exchange)
      .flatMap(sellerId -> sellerService.applyStore(sellerId, store));
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
