package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.configcenter.service.GlobalConfigService;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.query.vo.AdNewApplyPageVO;
import io.spm.parknshop.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class AdApplyRenderService {

  @Autowired
  private StoreService storeService;
  @Autowired
  private ProductService productService;
  @Autowired
  private GlobalConfigService globalConfigService;

  public Mono<AdNewApplyPageVO> renderNewApplyFor(Long sellerId) {
    if (Objects.isNull(sellerId) || sellerId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("sellerId"));
    }
    return storeService.getBySellerId(sellerId)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(STORE_NOT_EXIST, "You do not own a store")))
      .flatMap(store -> productService.getByStoreId(store.getId())
        .collectList()
        .flatMap(products -> globalConfigService.getAdPrice()
          .map(price -> new AdNewApplyPageVO().setStore(store).setProducts(products)
            .setProductAdPrice(price.r1).setStoreAdPrice(price.r2))
        )
      );
  }
}
