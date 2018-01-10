package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.query.vo.RefundVO;
import io.spm.parknshop.query.vo.apply.RefundApplyVO;
import io.spm.parknshop.query.vo.SimpleStoreVO;
import io.spm.parknshop.refund.service.RefundDataService;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class RefundQueryService {

  @Autowired
  private RefundDataService refundDataService;

  @Autowired
  private OrderProductRepository orderProductRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private ProductRepository productRepository;

  public Mono<RefundVO> renderRefundView(Long refundId) {
    return refundDataService.getRefundRecordById(refundId)
      .flatMap(refundRecord -> renderRefundApply(refundRecord.getSubOrderId())
        .map(refundApplyVO -> new RefundVO(refundRecord, refundApplyVO.getOrder(), refundApplyVO.getSubOrder(), refundApplyVO.getStore(), refundApplyVO.getUsername()))
      );
  }

  public Flux<RefundVO> getRefundByShop(Long storeId) {
    return refundDataService.getRefundRecordByStoreId(storeId)
      .concatMap(refundRecord -> renderRefundApply(refundRecord.getSubOrderId())
        .map(refundApplyVO -> new RefundVO(refundRecord, refundApplyVO.getOrder(), refundApplyVO.getSubOrder(), refundApplyVO.getStore(), refundApplyVO.getUsername()))
      );
  }

  public Flux<RefundVO> getRefundByCustomer(Long userId) {
    return refundDataService.getRefundRecordByUserId(userId)
      .concatMap(refundRecord -> renderRefundApply(refundRecord.getSubOrderId())
        .map(refundApplyVO -> new RefundVO(refundRecord, refundApplyVO.getOrder(), refundApplyVO.getSubOrder(), refundApplyVO.getStore(), refundApplyVO.getUsername()))
      );
  }

  public Mono<RefundApplyVO> renderRefundApply(Long subOrderId) {
    return getSubOrderById(subOrderId)
      .flatMap(subOrder -> async(() -> {
        Order order = orderRepository.findById(subOrder.getOrderId()).get();
        String username = userRepository.findById(order.getCreatorId()).map(User::getUsername).orElse("(Deleted User)");
        SimpleStoreVO store = SimpleStoreVO.fromStore(storeRepository.findById(order.getStoreId()).orElse(Store.deletedStore(order.getStoreId())));
        return new RefundApplyVO(order, subOrder, username, store);
      }));
  }

  private Optional<OrderProduct> retrieveSubOrder(long subOrderId) {
    return orderProductRepository.findById(subOrderId)
      .map(subOrder -> {
        Product product = productRepository.findByIdWithDeleted(subOrder.getProductId()).get();
        return subOrder.setProductStatus(product.getStatus()).setPicUri(product.getPicUri());
      });
  }

  private Mono<OrderProduct> getSubOrderById(long subOrderId) {
    return async(() -> retrieveSubOrder(subOrderId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.ORDER_UNEXPECTED_DATA, "Bad sub order id")));
  }
}
