package io.spm.parknshop.query.service;

import io.spm.parknshop.query.vo.ShopSaleVO;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public interface ShopSaleQueryService {

  Mono<ShopSaleVO> getSaleByStore(Long storeId);

  Mono<ShopSaleVO> getRangeSaleByStore(Long storeId, Date from, Date to);

  Mono<ShopSaleVO> getSaleBySeller(Long seller);
}
