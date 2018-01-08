package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.util.DateUtils;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.domain.SubOrderStatus;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.query.repository.OrderQueryRepository;
import io.spm.parknshop.query.service.OrderQueryService;
import io.spm.parknshop.query.vo.OrderVO;
import io.spm.parknshop.query.vo.ShopIncomeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class ShopIncomeQueryService {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderQueryRepository orderQueryRepository;

  private double getTotalRawSaleIncomeForStore(long storeId) {
    Double d = orderRepository.getTotalRawSaleIncomeForStore(storeId);
    return d == null ? 0 : d;
  }

  private double getTotalProfitForStore(long storeId) {
    Double d = orderRepository.getTotalProfitForStore(storeId);
    return d == null ? 0 : d;
  }

  public Mono<ShopIncomeVO> getTotalIncomeForShop(Long storeId) {
    return async(() -> {
      double totalIncome = getTotalRawSaleIncomeForStore(storeId);
      double totalProfit = getTotalProfitForStore(storeId);
      List<OrderVO> orderList = orderQueryRepository.queryFinishedOrderByStore(storeId).stream()
        .map(order -> {
          double refundFee = order.getProducts().stream()
            .filter(subOrder -> subOrder.getStatus() == SubOrderStatus.ALREADY_REFUND)
            .map(OrderProduct::getTotalPrice)
            .mapToDouble(e -> e)
            .sum();
          return order.setRefundFee(refundFee);
        }).collect(Collectors.toList());
      int total = orderList.stream().flatMap(e -> e.getProducts().stream())
        .filter(subOrder -> subOrder.getStatus() == SubOrderStatus.NORMAL)
        .map(OrderProduct::getAmount)
        .mapToInt(e -> e)
        .sum();
      return new ShopIncomeVO().setTotalProfit(totalProfit).setTotalRawIncome(totalIncome)
        .setProductSaleAmount(total).setOrderList(orderList);
    });
  }

  public Mono<ShopIncomeVO> getIncomeForShopDaily(Long storeId) {
    return getIncomeForShopBetween(storeId, DateUtils.toDate(LocalDate.now()), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<ShopIncomeVO> getIncomeForShopWeekly(Long storeId) {
    return getIncomeForShopBetween(storeId, DateUtils.toDate(LocalDate.now().minusDays(7)), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<ShopIncomeVO> getIncomeForShopMonthly(Long storeId) {
    return getIncomeForShopBetween(storeId, DateUtils.toDate(LocalDate.now().minusMonths(1)), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<ShopIncomeVO> getIncomeForShopYearly(Long storeId) {
    return getIncomeForShopBetween(storeId, DateUtils.toDate(LocalDate.now().minusYears(1)), DateUtils.toDate(LocalDate.now()));
  }

  private double getRawSaleIncomeForStoreBetween(long storeId, Date from, Date to) {
    Double d = orderRepository.getRawSaleIncomeForStoreBetween(storeId, from, to);
    return d == null ? 0 : d;
  }

  private double getProfitForStoreBetween(long storeId, Date from, Date to) {
    Double d = orderRepository.getProfitForStoreBetween(storeId, from, to);
    return d == null ? 0 : d;
  }

  public Mono<ShopIncomeVO> getIncomeForShopBetween(Long storeId, Date start, Date end) {
    if (start.after(end)) {
      return Mono.error(ExceptionUtils.invalidParam("start cannot be after end date"));
    }
    return async(() -> {
      double totalIncome = getRawSaleIncomeForStoreBetween(storeId, start, end);
      double totalProfit = getProfitForStoreBetween(storeId, start, end);
      List<OrderVO> orderList = orderQueryRepository.queryFinishedOrderByStoreBetween(storeId, start, end).stream()
        .map(this::wrapWithRefundFee)
        .collect(Collectors.toList());
      int total = orderList.stream().flatMap(e -> e.getProducts().stream())
        .filter(subOrder -> subOrder.getStatus() == SubOrderStatus.NORMAL)
        .map(OrderProduct::getAmount)
        .mapToInt(e -> e)
        .sum();
      return new ShopIncomeVO().setStart(start).setEnd(end).setTotalProfit(totalProfit).setTotalRawIncome(totalIncome)
        .setProductSaleAmount(total).setOrderList(orderList);
    });
  }

  private OrderVO wrapWithRefundFee(OrderVO order) {
    double refundFee = order.getProducts().stream()
      .filter(subOrder -> subOrder.getStatus() == SubOrderStatus.ALREADY_REFUND)
      .map(OrderProduct::getTotalPrice)
      .mapToDouble(e -> e)
      .sum();
    return order.setRefundFee(refundFee);
  }
}
