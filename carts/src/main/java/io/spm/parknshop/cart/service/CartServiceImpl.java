package io.spm.parknshop.cart.service;

import io.spm.parknshop.cart.domain.CartEvent;
import io.spm.parknshop.cart.domain.SimpleCartProduct;
import io.spm.parknshop.cart.domain.ShoppingCart;
import io.spm.parknshop.cart.repository.CartRepository;
import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

/**
 * @author Eric Zhao
 * @author four
 */
@Service
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ProductService productService;

  @Override
  public Mono<ShoppingCart> addCart(Long userId, CartEvent cartEvent) {
    return checkParams(userId, cartEvent)
        .flatMap(v -> checkProductExists(cartEvent.getProductId()))
        .flatMap(v -> cartRepository.getCartProduct(userId, cartEvent.getProductId())
            .map(e -> e.plusAmount(cartEvent.getAmount()))
            .switchIfEmpty(newCreated(cartEvent))
            .flatMap(e -> cartRepository.putCartProduct(userId, e))
            .flatMap(e -> getCartForUser(userId))
        );
  }

  @Override
  public Mono<ShoppingCart> deleteCart(Long userId, CartEvent cartEvent) {
    return checkParams(userId, cartEvent)
        .flatMap(v -> checkProductExists(cartEvent.getProductId()))
        .flatMap(v -> cartRepository.getCartProduct(userId, cartEvent.getProductId())
            .map(e -> e.decreaseAmount(cartEvent.getAmount()))
            .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.PRODUCT_NOT_EXIST_IN_CART, "Product is not in your cart")))
            .flatMap(e -> doDelete(userId, e))
            .flatMap(e -> getCartForUser(userId))
        );
  }

  @Override
  public Mono<ShoppingCart> getCartForUser(Long userId) {
    return Mono.just(new ShoppingCart());
  }

  private Mono<?> checkProductExists(Long productId) {
    return productService.getById(productId)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.PRODUCT_NOT_EXIST, "Product does not exist")));
  }

  private Mono<?> doDelete(/*@NonNull*/ Long userId, SimpleCartProduct cartProduct) {
    if (cartProduct.getAmount() <= 0) {
      return cartRepository.deleteCartProduct(userId, cartProduct.getId());
    } else {
      return cartRepository.putCartProduct(userId, cartProduct);
    }
  }

  private Mono<SimpleCartProduct> newCreated(CartEvent cartEvent) {
    return Mono.just(new SimpleCartProduct(cartEvent.getProductId(), cartEvent.getAmount()));
  }

  private Mono<?> checkParams(Long userId, CartEvent cartEvent) {
    return checkUserId(userId)
        .flatMap(v -> checkCartEvent(cartEvent));
  }

  private Mono<?> checkCartEvent(CartEvent cartEvent) {
    return Optional.ofNullable(cartEvent)
        .map(e -> cartEvent.getProductId())
        .map(e -> cartEvent.getAmount())
        .map(Mono::just)
        .orElse(Mono.error(ExceptionUtils.invalidParam("cart operation")));
  }

  private Mono<?> checkUserId(Long userId) {
    if (Objects.isNull(userId) || userId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return Mono.just(userId);
  }
}
