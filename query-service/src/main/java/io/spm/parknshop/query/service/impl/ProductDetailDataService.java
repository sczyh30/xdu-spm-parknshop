package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.favorite.service.FavoriteService;
import io.spm.parknshop.query.vo.ProductDetailUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductDetailDataService {

  @Autowired
  private FavoriteService favoriteService;

  public Mono<ProductDetailUserVO> getData(Long userId, Long productId) {
    return favoriteService.checkUserLikeProduct(userId, productId)
      .map(e -> new ProductDetailUserVO().setUserId(userId).setProductId(productId).setInFavorite(e));
  }
}
