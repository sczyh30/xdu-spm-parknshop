package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.exception.ServiceException;
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

  @Override
  public Mono<ShopSaleVO> getSaleByStore(Long storeId) {
    return async(() -> calcSaleInternal(storeId));
  }

  private ShopSaleVO calcSaleInternal(long storeId) {
    double totalPrice = orderRepository.getSaleMoneyForStore(storeId);
    double totalProfit = orderRepository.getSaleProfitForStore(storeId) / 100;
    return new ShopSaleVO().setTotalProfit(totalProfit).setTotalSale(totalPrice);
  }

  @Override
  public Mono<ShopSaleVO> getRangeSaleByStore(Long storeId, Date from, Date to) {
    return null;
  }

  @Override
  public Mono<ShopSaleVO> getSaleBySeller(Long seller) {
    return async(() -> storeRepository.getBySellerId(seller)
      .map(e -> calcSaleInternal(e.getId())))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(STORE_NOT_EXIST, "The seller does not own a shop")));
  }
}
