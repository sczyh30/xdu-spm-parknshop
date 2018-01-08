package io.spm.parknshop.api.controller;

import io.spm.parknshop.query.service.impl.AppIncomeService;
import io.spm.parknshop.query.vo.AppIncomeVO;
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
public class WebsiteIncomeApiController {

  @Autowired
  private AppIncomeService appIncomeService;

  @GetMapping("/website_income/all/range")
  public Mono<AppIncomeVO> apiGetIncomeForShopBetween(@RequestParam(name = "from") Long from, @RequestParam(name = "to") Long to) {
    return appIncomeService.getWebsiteIncomeBetween(new Date(from), new Date(to));
  }

  @GetMapping("/website_income/all/total")
  public Mono<AppIncomeVO> apiGetTotalIncomeForShop() {
    return appIncomeService.getTotalWebsiteIncome();
  }

  @GetMapping("/website_income/all/daily")
  public Mono<AppIncomeVO> apiGetDailyIncomeForShop() {
    return appIncomeService.getWebsiteIncomeDaily();
  }

  @GetMapping("/website_income/all/weekly")
  public Mono<AppIncomeVO> apiGetWeeklyIncomeForShop() {
    return appIncomeService.getWebsiteIncomeWeekly();
  }

  @GetMapping("/website_income/all/monthly")
  public Mono<AppIncomeVO> apiGetMonthlyIncomeForShop() {
    return appIncomeService.getWebsiteIncomeMonthly();
  }

  @GetMapping("/website_income/all/yearly")
  public Mono<AppIncomeVO> apiGetYearlyIncomeForShop() {
    return appIncomeService.getWebsiteIncomeYearly();
  }
}
