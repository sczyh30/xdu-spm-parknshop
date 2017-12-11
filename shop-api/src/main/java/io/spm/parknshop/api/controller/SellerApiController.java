package io.spm.parknshop.api.controller;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.seller.service.SellerService;
import io.spm.parknshop.store.domain.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
public class SellerApiController {

  @Autowired
  private SellerService sellerService;

  @PostMapping("/seller/apply_store")
  public /*Mono<String>*/ Mono<?> applyStore(@RequestBody Store store) {
    if (store == null || store.getSellerId() == null) {
      return Mono.error(ExceptionUtils.invalidParam("store"));
    }
    return sellerService.applyStore(store.getSellerId(), store);
  }

}
