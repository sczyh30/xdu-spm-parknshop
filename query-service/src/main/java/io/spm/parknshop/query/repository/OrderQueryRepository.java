package io.spm.parknshop.query.repository;

import io.spm.parknshop.delivery.repository.DeliveryAddressRepository;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.query.vo.OrderVO;
import io.spm.parknshop.query.vo.SimpleStoreVO;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
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

  @Transactional(readOnly = true)
  public Optional<OrderVO> queryOrder(long orderId) {
    return orderRepository.findById(orderId)
      .flatMap(this::buildOrderVO);
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryOrderByUser(long userId) {
    return orderRepository.getByCreatorId(userId).stream()
      .map(this::buildRawOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryOrderByStore(long storeId) {
    return orderRepository.getByStoreId(storeId).stream()
      .map(this::buildRawOrderVO)
      .collect(Collectors.toList());
  }

  private OrderVO buildRawOrderVO(final Order order) {
    return buildOrderVO(order).orElse(new OrderVO().setOrder(order).setId(order.getId()));
  }

  private Optional<OrderVO> buildOrderVO(final Order order) {
    return storeRepository.findById(order.getStoreId())
      .map(this::fromStore)
      .flatMap(store -> deliveryAddressRepository.findById(order.getAddressId())
        .map(address -> new OrderVO(order.getId(), order, store, orderProductRepository.getByOrderId(order.getId()), address))
      );
  }

  private SimpleStoreVO fromStore(Store store) {
    return new SimpleStoreVO().setSellerId(store.getSellerId())
      .setStoreId(store.getId())
      .setStoreName(store.getName())
      .setSellerTelephone(store.getTelephone());
  }
}
