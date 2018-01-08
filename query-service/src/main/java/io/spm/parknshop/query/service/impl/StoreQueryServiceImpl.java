package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.query.service.StoreQueryService;
import io.spm.parknshop.query.vo.StoreVO;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class StoreQueryServiceImpl implements StoreQueryService {

  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private UserRepository userRepository;

  @Override
  public Mono<StoreVO> getStoreById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> queryStoreVO(id))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.STORE_NOT_EXIST, "Store does not exist")));
  }

  @Override
  public Mono<StoreVO> getStoreBySeller(Long id) {
    return async(() -> queryStoreBySellerId(id))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.STORE_NOT_EXIST, "The seller does not own a store")));
  }

  @Override
  public Flux<StoreVO> getAll() {
    return asyncIterable(() -> storeRepository.findAll().stream().map(this::toStoreVO).collect(Collectors.toList()));

  }

  protected StoreVO toStoreVO(Store store) {
    long storeId = store.getId();
    // TODO: Check
    return new StoreVO(storeId, store, userRepository.getSellerById(store.getSellerId()).get())
      .setOrderAmount(orderRepository.countByStoreId(storeId))
      .setProductAmount(productRepository.countByStoreId(storeId));
  }

  protected Optional<StoreVO> queryStoreBySellerId(long sellerId) {
    return storeRepository.getBySellerId(sellerId)
      .flatMap(store -> userRepository.getSellerById(sellerId)
        .map(seller -> new StoreVO(store.getId(), store, seller)
          .setOrderAmount(orderRepository.countByStoreId(store.getId()))
          .setProductAmount(productRepository.countByStoreId(store.getId()))
        )
      );
  }

  protected Optional<StoreVO> queryStoreVO(long id) {
    return storeRepository.findById(id)
      .flatMap(store -> userRepository.getSellerById(store.getSellerId())
        .map(seller -> new StoreVO(id, store, seller)
          .setOrderAmount(orderRepository.countByStoreId(id))
          .setProductAmount(productRepository.countByStoreId(id))
        )
      );
  }
}
