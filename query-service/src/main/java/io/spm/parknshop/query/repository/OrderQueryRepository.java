package io.spm.parknshop.query.repository;

import io.spm.parknshop.delivery.repository.DeliveryAddressRepository;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.payment.repository.PaymentRecordRepository;
import io.spm.parknshop.query.vo.OrderVO;
import io.spm.parknshop.query.vo.SimpleStoreVO;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderQueryRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private OrderProductRepository orderProductRepository;
  @Autowired
  private StoreRepository storeRepository;
  @Autowired
  private DeliveryAddressRepository deliveryAddressRepository;
  @Autowired
  private PaymentRecordRepository paymentRecordRepository;
  @Autowired
  private UserRepository userRepository;

  @Transactional(readOnly = true)
  public Optional<OrderVO> queryOrder(long orderId) {
    return orderRepository.findById(orderId)
      .flatMap(this::buildOrderVO);
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryOrderByUser(long userId) {
    return orderRepository.getByCreatorIdOrderByIdDesc(userId).stream()
      .map(this::buildRawOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryOrderByStore(long storeId) {
    return orderRepository.getByStoreIdOrderByIdDesc(storeId).stream()
      .map(this::buildRawOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryFinishedOrderByStore(long storeId) {
    return orderRepository.getFinishedByStoreId(storeId).stream()
      .map(this::buildRawOrderVO)
      .collect(Collectors.toList());
  }

  private OrderVO buildRawOrderVO(final Order order) {
    return buildOrderVO(order).orElse(new OrderVO().setOrder(order).setId(order.getId()));
  }

  @Transactional(readOnly = true)
  protected Optional<OrderVO> buildOrderVO(final Order order) {
    return storeRepository.findById(order.getStoreId())
      .map(this::fromStore)
      .flatMap(store -> deliveryAddressRepository.findById(order.getAddressId())
        .flatMap(address -> paymentRecordRepository.findById(order.getPaymentId())
          .flatMap(payment -> userRepository.findById(order.getCreatorId())
            .map(user -> new OrderVO(order.getId(), order, store, orderProductRepository.getByOrderId(order.getId()), address).setPayment(payment).setUser(user))
          )
        )
      );
  }

  private SimpleStoreVO fromStore(Store store) {
    return new SimpleStoreVO().setSellerId(store.getSellerId())
      .setStoreId(store.getId())
      .setStoreName(store.getName())
      .setStoreTelephone(store.getTelephone())
      .setStoreEmail(store.getEmail());
  }
}
