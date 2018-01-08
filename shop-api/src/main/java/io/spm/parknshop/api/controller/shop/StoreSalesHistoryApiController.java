package io.spm.parknshop.api.controller.shop;

import io.spm.parknshop.query.service.impl.ShopOrderSaleHistoryQueryService;
import io.spm.parknshop.query.vo.ShopSaleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;


/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class StoreSalesHistoryApiController {

  @Autowired
  private ShopOrderSaleHistoryQueryService shopOrderSaleHistoryQueryService;

  @GetMapping("/sale_history/shop/query_total/{id}")
  public Mono<ShopSaleVO> apiGetTotalIncomeForShop(@PathVariable("id") Long storeId) {
    return shopOrderSaleHistoryQueryService.getTotalSaleByStore(storeId);
  }

  @GetMapping("/sale_history/shop/query/{id}")
  public Mono<ShopSaleVO> apiGetIncomeForShopBetween(@PathVariable("id") Long storeId,
                                                       @RequestParam(name = "from") Long from, @RequestParam(name = "to") Long to) {
    return shopOrderSaleHistoryQueryService.getSaleHistoryByStoreBetween(storeId, new Date(from), new Date(to));
  }

  @GetMapping("/sale_history/shop/daily/{id}")
  public Mono<ShopSaleVO> apiGetDailyIncomeForShop(@PathVariable("id") Long storeId) {
    return shopOrderSaleHistoryQueryService.getSaleHistoryByStoreDaily(storeId);
  }

  @GetMapping("/sale_history/shop/weekly/{id}")
  public Mono<ShopSaleVO> apiGetWeeklyIncomeForShop(@PathVariable("id") Long storeId) {
    return shopOrderSaleHistoryQueryService.getSaleHistoryByStoreWeekly(storeId);
  }

  @GetMapping("/sale_history/shop/monthly/{id}")
  public Mono<ShopSaleVO> apiGetMonthlyIncomeForShop(@PathVariable("id") Long storeId) {
    return shopOrderSaleHistoryQueryService.getSaleHistoryByStoreMonthly(storeId);
  }

  @GetMapping("/sale_history/shop/yearly/{id}")
  public Mono<ShopSaleVO> apiGetYearlyIncomeForShop(@PathVariable("id") Long storeId) {
    return shopOrderSaleHistoryQueryService.getSaleHistoryByStoreYearly(storeId);
  }
}
