package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.order.service.OrderService;
import io.spm.parknshop.query.vo.RefundApplyVO;
import io.spm.parknshop.query.vo.SimpleStoreVO;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class RefundQueryService {

  @Autowired
  private OrderProductRepository orderProductRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private StoreRepository storeRepository;

  public Mono<RefundApplyVO> renderRefundApply(Long subOrderId) {
    return getSubOrderById(subOrderId)
      .flatMap(subOrder -> async(() -> {
        Order order = orderRepository.findById(subOrder.getOrderId()).get();
        String username = userRepository.findById(order.getCreatorId()).map(User::getUsername).orElse("(Deleted User)");
        SimpleStoreVO store = SimpleStoreVO.fromStore(storeRepository.findById(order.getStoreId()).orElse(Store.deletedStore(order.getStoreId())));
        return new RefundApplyVO(order, subOrder, username, store);
      }));
  }

  private Mono<OrderProduct> getSubOrderById(long subOrderId) {
    return async(() -> orderProductRepository.findById(subOrderId))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.ORDER_UNEXPECTED_DATA, "Bad sub order id")));
  }
}
