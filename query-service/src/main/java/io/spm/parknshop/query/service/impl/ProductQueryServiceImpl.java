package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.domain.ProductVO;
import io.spm.parknshop.product.repository.ProductQueryRepository;
import io.spm.parknshop.query.service.ProductQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class ProductQueryServiceImpl implements ProductQueryService {

  @Autowired
  private ProductQueryRepository productQueryRepository;

  @Override
  public Flux<ProductVO> getUserFavoriteProducts(Long userId) {
    if (Objects.isNull(userId) || userId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("userId"));
    }
    return asyncIterable(() -> productQueryRepository.getByUserFavorite(userId));
  }
}
