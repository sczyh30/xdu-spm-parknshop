package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.comment.service.CommentService;
import io.spm.parknshop.favorite.service.FavoriteService;
import io.spm.parknshop.query.vo.ProductDetailUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProductDetailDataService {

  @Autowired
  private FavoriteService favoriteService;
  @Autowired
  private CommentService commentService;

  public Mono<ProductDetailUserVO> getData(Long userId, Long productId) {
    return favoriteService.checkUserLikeProduct(userId, productId)
      .flatMap(inFavorite -> commentService.canComment(userId, productId)
        .map(canComment -> new ProductDetailUserVO(userId, productId, inFavorite, canComment))
      );
  }
}
