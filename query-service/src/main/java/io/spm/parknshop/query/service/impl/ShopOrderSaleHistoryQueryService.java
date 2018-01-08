package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.util.DateUtils;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.query.repository.OrderQueryRepository;
import io.spm.parknshop.query.vo.OrderVO;
import io.spm.parknshop.query.vo.ShopSaleVO;
import io.spm.parknshop.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * @author Eric Zhao
 */
@Service
public class ShopOrderSaleHistoryQueryService {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderQueryRepository orderQueryRepository;
  @Autowired
  private StoreRepository storeRepository;

  public Mono<ShopSaleVO> getTotalSaleByStore(Long storeId) {
    return async(() -> {
      List<OrderVO> orderList = orderQueryRepository.queryPaidOrderByStore(storeId);
      double totalIncome = calcTotalIncome(orderList);
      return new ShopSaleVO(null, null, orderList, totalIncome);
    });
  }

  public Mono<ShopSaleVO> getSaleHistoryByStoreBetween(Long storeId, Date start, Date end) {
    if (start.after(end)) {
      return Mono.error(ExceptionUtils.invalidParam("start cannot be after end date"));
    }
    return async(() -> {
      List<OrderVO> orderList = orderQueryRepository.queryPaidOrderByStoreBetween(storeId, start, end);
      double totalIncome = calcTotalIncome(orderList);
      return new ShopSaleVO(start, end, orderList, totalIncome);
    });
  }

  public Mono<ShopSaleVO> getSaleHistoryByStoreDaily(Long storeId) {
    return getSaleHistoryByStoreBetween(storeId, DateUtils.toDate(LocalDate.now()), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<ShopSaleVO> getSaleHistoryByStoreWeekly(Long storeId) {
    return getSaleHistoryByStoreBetween(storeId, DateUtils.toDate(LocalDate.now().minusDays(7)), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<ShopSaleVO> getSaleHistoryByStoreMonthly(Long storeId) {
    return getSaleHistoryByStoreBetween(storeId, DateUtils.toDate(LocalDate.now().minusMonths(1)), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<ShopSaleVO> getSaleHistoryByStoreYearly(Long storeId) {
    return getSaleHistoryByStoreBetween(storeId, DateUtils.toDate(LocalDate.now().minusYears(1)), DateUtils.toDate(LocalDate.now()));
  }

  private double calcTotalIncome(List<OrderVO> orderList) {
    return orderList.stream()
      .mapToDouble(e -> e.getOrder().getFinalTotalPrice() - e.getOrder().getFreightPrice())
      .sum();
  }
}
