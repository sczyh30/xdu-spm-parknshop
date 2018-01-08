package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.repository.AdvertisementRepository;
import io.spm.parknshop.common.util.DateUtils;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.domain.SubOrderStatus;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.query.repository.OrderQueryRepository;
import io.spm.parknshop.query.vo.AppIncomeItem;
import io.spm.parknshop.query.vo.AppIncomeVO;
import io.spm.parknshop.query.vo.OrderVO;
import io.spm.parknshop.query.vo.StoreVO;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static io.spm.parknshop.common.exception.ErrorConstants.*;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class AppIncomeService {

  @Autowired
  private AdvertisementRepository advertisementRepository;
  @Autowired
  private OrderQueryRepository orderQueryRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private OrderRepository orderRepository;

  public Mono<AppIncomeVO> getWebsiteIncomeDaily() {
    return getWebsiteIncomeBetween(DateUtils.toDate(LocalDate.now()), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<AppIncomeVO> getWebsiteIncomeWeekly() {
    return getWebsiteIncomeBetween(DateUtils.toDate(LocalDate.now().minusDays(7)), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<AppIncomeVO> getWebsiteIncomeMonthly() {
    return getWebsiteIncomeBetween(DateUtils.toDate(LocalDate.now().minusMonths(1)), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<AppIncomeVO> getWebsiteIncomeYearly() {
    return getWebsiteIncomeBetween(DateUtils.toDate(LocalDate.now().minusYears(1)), DateUtils.toDate(LocalDate.now()));
  }

  public Mono<AppIncomeVO> getWebsiteIncomeBetween(Date start, Date end) {
    if (start.after(end)) {
      return Mono.error(ExceptionUtils.invalidParam("start cannot be after end date"));
    }
    return async(() -> {
      List<Advertisement> advertisements = advertisementRepository.getBetweenCreateDateRange(start, end);
      List<OrderVO> orderList = orderQueryRepository.queryFinishedOrderBetween(start, end);
      List<AppIncomeItem> items = new ArrayList<>();
      advertisements.forEach(e -> items.add(wrapToIncome(e)));
      orderList.forEach(e -> items.add(wrapToIncome(e)));
      double totalProfit = items.stream().mapToDouble(AppIncomeItem::getProfit).sum();
      return new AppIncomeVO().setIncomeList(items).setTotalProfit(totalProfit).setStart(start).setEnd(end);
    });
  }

  public Mono<AppIncomeVO> getTotalWebsiteIncome() {
    return async(() -> {
      List<Advertisement> advertisements = advertisementRepository.findAll();
      List<OrderVO> orderList = orderQueryRepository.queryFinishedOrder();
      List<AppIncomeItem> items = new ArrayList<>();
      advertisements.forEach(e -> items.add(wrapToIncome(e)));
      orderList.forEach(e -> items.add(wrapToIncome(e)));
      double totalProfit = items.stream().mapToDouble(AppIncomeItem::getProfit).sum();
      return new AppIncomeVO().setIncomeList(items).setTotalProfit(totalProfit);
    });
  }

  private AppIncomeItem wrapToIncome(Advertisement advertisement) {
    return new AppIncomeItem().setProfit(advertisement.getAdTotalPrice())
      .setType(AD).setTargetId(advertisement.getId()).setTargetObj(advertisement)
      .setStore(querySimpleStoreVOBySellerId(advertisement.getAdOwner()));
  }

  private AppIncomeItem wrapToIncome(OrderVO orderVO) {
    double totalPrice = orderVO.getProducts().stream()
      .filter(e -> e.getStatus() != SubOrderStatus.ALREADY_REFUND)
      .mapToDouble(OrderProduct::getTotalPrice)
      .sum() * orderVO.getOrder().getCommissionSnapshot() / 100.0;
    return new AppIncomeItem().setProfit(totalPrice)
      .setType(BUY).setTargetId(orderVO.getId()).setTargetObj(orderVO)
      .setStore(querySimpleStoreVOBySellerId(orderVO.getStore().getSellerId()));
  }

  @Transactional(readOnly = true)
  protected StoreVO querySimpleStoreVOBySellerId(long sellerId) {
    Store store = storeRepository.getBySellerId(sellerId).orElse(Store.deletedStore(sellerId));
    User seller = userRepository.getSellerById(sellerId).orElse(User.deletedUser(sellerId));
    return new StoreVO().setSeller(seller).setStore(store);
  }

  public static final int BUY = 1;
  public static final int AD = 2;
}
