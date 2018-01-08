package io.spm.parknshop.query.repository;

import io.spm.parknshop.common.util.JsonUtils;
import io.spm.parknshop.delivery.domain.DeliveryAddress;
import io.spm.parknshop.delivery.domain.DeliveryTrackRecord;
import io.spm.parknshop.delivery.repository.DeliveryAddressRepository;
import io.spm.parknshop.delivery.repository.DeliveryRecordRepository;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.payment.repository.PaymentRecordRepository;
import io.spm.parknshop.product.repository.ProductRepository;
import io.spm.parknshop.query.vo.OrderVO;
import io.spm.parknshop.query.vo.SimpleStoreVO;
import io.spm.parknshop.refund.repository.RefundRecordRepository;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.repository.StoreRepository;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Eric Zhao
 */
@Repository
public class OrderQueryRepository {

  @Autowired
  private ProductRepository productRepository;
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
  @Autowired
  private RefundRecordRepository refundRecordRepository;
  @Autowired
  private DeliveryRecordRepository deliveryRecordRepository;

  @Transactional(readOnly = true)
  public Optional<OrderVO> queryOrder(long orderId) {
    return orderRepository.findById(orderId)
      .map(this::buildOrderVO);
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryOrderByUser(long userId) {
    return orderRepository.getByCreatorIdOrderByIdDesc(userId).stream()
      .map(this::buildOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryOrderByUserBetween(long userId, Date from, Date to) {
    return orderRepository.getByCreatorIdBetween(userId, from, to).stream()
      .map(this::buildOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryOrderByStore(long storeId) {
    return orderRepository.getByStoreIdOrderByIdDesc(storeId).stream()
      .map(this::buildOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryPaidOrderByStore(long storeId) {
    return orderRepository.getPaidByStoreId(storeId).stream()
      .map(this::buildOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryPaidOrderByStoreBetween(long storeId, Date start, Date end) {
    return orderRepository.getPaidByStoreIdBetween(storeId, start, end).stream()
      .map(this::buildOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryFinishedOrderByStore(long storeId) {
    return orderRepository.getFinishedByStoreId(storeId).stream()
      .map(this::buildOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<OrderVO> queryFinishedOrderByStoreBetween(long storeId, Date start, Date end) {
    return orderRepository.getFinishedByStoreIdBetween(storeId, start, end).stream()
      .map(this::buildOrderVO)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  protected OrderVO buildOrderVO(final Order order) {
    Long orderId = order.getId();
    SimpleStoreVO store = SimpleStoreVO.fromStore(storeRepository.findById(order.getStoreId()).orElse(Store.deletedStore(order.getStoreId())));
    DeliveryAddress address = JsonUtils.parse(order.getAddressSnapshot(), DeliveryAddress.class);
    PaymentRecord payment = paymentRecordRepository.findById(order.getPaymentId()).orElse(null);
    User user = userRepository.findById(order.getCreatorId()).orElse(User.deletedUser(order.getCreatorId()));
    DeliveryTrackRecord deliveryTrackRecord = deliveryRecordRepository.getByOrderId(orderId).orElse(null);
    return new OrderVO(orderId, order, store, retrieveSubOrders(orderId), payment, address, user)
      .setDelivery(deliveryTrackRecord);
  }

  @Transactional(readOnly = true)
  protected List<OrderProduct> retrieveSubOrders(long orderId) {
    return orderProductRepository.getByOrderId(orderId).stream()
      .map(subOrder -> productRepository.findByIdWithDeleted(subOrder.getProductId()).map(product ->
        subOrder.setProductStatus(product.getStatus()).setPicUri(product.getPicUri())).get())
      .map(subOrder -> refundRecordRepository.getCurrentRefundBySubOrderId(subOrder.getId())
        .map(refundRecord -> subOrder.setRefundId(refundRecord.getId())).orElse(subOrder)
      )
      .collect(Collectors.toList());
  }
}
