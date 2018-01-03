package io.spm.parknshop.configcenter.service;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author Eric Zhao
 */
@Service
public class GlobalConfigService {

  @Autowired
  private ReactiveRedisTemplate<String, String> template;

  public Mono<Boolean> setProductAdPrice(Double price) {
    if (Objects.isNull(price) || price <= 0d) {
      return Mono.error(ExceptionUtils.invalidParam("price"));
    }
    return template.opsForValue()
      .set(PRODUCT_AD_PRICE_KEY, String.valueOf(price));
  }

  /**
   * Format: (product price, shop price)
   */
  public Mono<Tuple2<Double, Double>> getAdPrice() {
    return getProductAdPrice()
      .flatMap(productPrice -> getShopAdPrice().map(shopPrice -> Tuple2.of(productPrice, shopPrice)));
  }

  public Mono<Double> getProductAdPrice() {
    return template.opsForValue()
      .get(PRODUCT_AD_PRICE_KEY)
      .map(Double::valueOf)
      .switchIfEmpty(Mono.just(DEFAULT_PRODUCT_AD_PRICE));
  }

  public Mono<Boolean> setShopAdPrice(Double price) {
    if (Objects.isNull(price) || price <= 0d) {
      return Mono.error(ExceptionUtils.invalidParam("price"));
    }
    return template.opsForValue()
      .set(SHOP_AD_PRICE_KEY, String.valueOf(price));
  }

  public Mono<Double> getShopAdPrice() {
    return template.opsForValue()
      .get(SHOP_AD_PRICE_KEY)
      .map(Double::valueOf)
      .switchIfEmpty(Mono.just(DEFAULT_SHOP_AD_PRICE));
  }

  public Mono<Boolean> setCommission(Double commission) {
    if (Objects.isNull(commission) || commission <= 0d || commission >= 100d) {
      return Mono.error(ExceptionUtils.invalidParam("commission"));
    }
    return template.opsForValue()
      .set(COMMISSION_KEY, String.valueOf(commission));
  }

  public Mono<Double> getCommission() {
    return template.opsForValue()
      .get(COMMISSION_KEY)
      .map(Double::valueOf)
      .switchIfEmpty(Mono.just(DEFAULT_COMMISSION));
  }

  private static final double DEFAULT_COMMISSION = 2.00d;
  private static final double DEFAULT_PRODUCT_AD_PRICE = 1000.00d;
  private static final double DEFAULT_SHOP_AD_PRICE = 500.00d;

  private static final String COMMISSION_KEY = "global_config:commission_rate";
  private static final String PRODUCT_AD_PRICE_KEY = "global_config:product_ad_price";
  private static final String SHOP_AD_PRICE_KEY = "global_config:shop_ad_price";
}
