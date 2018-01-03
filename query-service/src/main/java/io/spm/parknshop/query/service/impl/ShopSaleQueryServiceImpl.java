package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.configcenter.service.GlobalConfigService;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.query.service.ShopSaleQueryService;
import io.spm.parknshop.query.vo.ShopSaleVO;
import io.spm.parknshop.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class ShopSaleQueryServiceImpl implements ShopSaleQueryService {

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private StoreRepository storeRepository;

  @Autowired
  private GlobalConfigService globalConfigService;

  @Override
  public Mono<ShopSaleVO> getSaleByStore(Long storeId) {
    // TODO: Here we should refactor!
    return globalConfigService.getCommission()
      .flatMap(c -> async(() -> calcSaleInternal(c, storeId)));
  }

  private ShopSaleVO calcSaleInternal(double c, long storeId) {
    double totalPrice = orderRepository.getSaleMoneyForStore(storeId);
    double totalProfit = c * totalPrice;
    return new ShopSaleVO().setCommission(c).setTotalProfit(totalProfit).setTotalSale(totalPrice);
  }

  @Override
  public Mono<ShopSaleVO> getRangeSaleByStore(Long storeId, Date from, Date to) {
    return null;
  }

  @Override
  public Mono<ShopSaleVO> getSaleBySeller(Long seller) {
    return globalConfigService.getCommission()
      .flatMap(c -> async(() -> storeRepository.getBySellerId(seller)
        .map(e -> calcSaleInternal(c, e.getId()))))
      .filter(Optional::isPresent)
      .map(Optional::get);
  }
}
