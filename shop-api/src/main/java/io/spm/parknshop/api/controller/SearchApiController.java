package io.spm.parknshop.api.controller;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.store.service.StoreService;
import io.spm.parknshop.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * Search controller.
 *
 * @author four
 */
@RestController
@RequestMapping("/api/v1/")
public class SearchApiController {

  @Autowired
  private UserService userService;
  @Autowired
  private SellerUserService sellerService;
  @Autowired
  private ProductService productService;
  @Autowired
  private StoreService storeService;

  @GetMapping("/search")
  public Publisher<?> apiSearch(@RequestParam("type") String type, @RequestParam("keyword") String keyword) {
    if (StringUtils.isEmpty(type)) {
      return Flux.error(ExceptionUtils.invalidParam("type"));
    }
    if ("user".equals(type)) {
      return userService.searchCustomerByKeyword(keyword);
    }
    if ("seller".equals(type)) {
      return sellerService.searchSellerByKeyword(keyword);
    }
    if ("item".equals(type)) {
      return productService.searchProductByKeyword(keyword);
    }
    if ("store".equals(type)) {
      return storeService.searchStoreByKeyword(keyword);
    }
    return Flux.error(ExceptionUtils.invalidParam("type"));
  }
}
