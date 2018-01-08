package io.spm.parknshop.api.controller.shop;

import io.spm.parknshop.query.service.impl.ShopIncomeQueryService;
import io.spm.parknshop.query.vo.ShopIncomeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class StoreIncomeApiController {

  @Autowired
  private ShopIncomeQueryService shopIncomeQueryService;

  @GetMapping("/income/shop/query_total/{id}")
  public Mono<ShopIncomeVO> apiGetTotalIncomeForShop(@PathVariable("id") Long storeId) {
    return shopIncomeQueryService.getTotalIncomeForShop(storeId);
  }

  @GetMapping("/income/shop/query/{id}")
  public Mono<ShopIncomeVO> apiGetIncomeForShopBetween(@PathVariable("id") Long storeId,
                                                     @RequestParam(name = "from") Long from, @RequestParam(name = "to") Long to) {
    return shopIncomeQueryService.getIncomeForShopBetween(storeId, new Date(from), new Date(to));
  }

  @GetMapping("/income/shop/daily/{id}")
  public Mono<ShopIncomeVO> apiGetDailyIncomeForShop(@PathVariable("id") Long storeId) {
    return shopIncomeQueryService.getIncomeForShopDaily(storeId);
  }

  @GetMapping("/income/shop/weekly/{id}")
  public Mono<ShopIncomeVO> apiGetWeeklyIncomeForShop(@PathVariable("id") Long storeId) {
    return shopIncomeQueryService.getIncomeForShopWeekly(storeId);
  }

  @GetMapping("/income/shop/monthly/{id}")
  public Mono<ShopIncomeVO> apiGetMonthlyIncomeForShop(@PathVariable("id") Long storeId) {
    return shopIncomeQueryService.getIncomeForShopMonthly(storeId);
  }

  @GetMapping("/income/shop/yearly/{id}")
  public Mono<ShopIncomeVO> apiGetYearlyIncomeForShop(@PathVariable("id") Long storeId) {
    return shopIncomeQueryService.getIncomeForShopYearly(storeId);
  }
}
