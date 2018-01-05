package io.spm.parknshop.trade.service.impl;

import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.common.util.JsonUtils;
import io.spm.parknshop.configcenter.service.GlobalConfigService;
import io.spm.parknshop.delivery.domain.DeliveryAddress;
import io.spm.parknshop.inventory.repository.InventoryRepository;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.domain.OrderProduct;
import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.service.OrderService;
import io.spm.parknshop.order.service.OrderStatusService;
import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.payment.service.PaymentService;
import io.spm.parknshop.trade.domain.ConfirmOrderMessage;
import io.spm.parknshop.trade.domain.PaymentResult;
import io.spm.parknshop.trade.domain.SubmitOrderResult;
import io.spm.parknshop.trade.domain.OrderStoreGroupUnit;
import io.spm.parknshop.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class TradeServiceImpl implements TradeService {

  @Autowired
  private OrderService orderService;
  @Autowired
  private OrderStatusService orderStatusService;
  @Autowired
  private PaymentService paymentService;
  @Autowired
  private GlobalConfigService commissionService;

  @Autowired
  private InventoryRepository inventoryRepository;
  @Autowired
  private OrderProductRepository orderProductRepository;

  @Override
  public Mono<SubmitOrderResult> dispatchAndProcessOrder(ConfirmOrderMessage confirmOrderMessage) {
    return checkRpcMessage(confirmOrderMessage)
      .flatMap(v -> paymentService.createPaymentRecord(confirmOrderMessage.getOrderPreview().getTotalPrice()))
      .flatMap(payment -> splitAndCreateOrders(confirmOrderMessage, payment)
        .flatMap(v -> startPayInternal(payment.getId()))
      );
  }

  private Mono<List<Order>> splitAndCreateOrders(ConfirmOrderMessage message, PaymentRecord paymentRecord) {
    List<Tuple2<Order, OrderStoreGroupUnit>> rawOrderList = message.getOrderPreview().getStoreGroups().stream()
      .map(e -> Tuple2.of(wrapRawOrder(e, paymentRecord, message.getDeliveryAddress(), message.getCreatorId()), e))
      .collect(Collectors.toList());
    // We need to save current commission snapshot.
    return commissionService.getCommission()
      .flatMap(commission -> Flux.fromIterable(rawOrderList)
        .flatMap(e -> orderService.createOrder(e.r1.setCommissionSnapshot(commission), e.r2))
        .collectList()
      );
  }

  private Order wrapRawOrder(OrderStoreGroupUnit unit, PaymentRecord paymentRecord, DeliveryAddress address, Long creatorId) {
    return new Order().setCreatorId(creatorId).setAddressSnapshot(JsonUtils.toJson(address))
      .setPaymentId(paymentRecord.getId()).setStoreId(unit.getStoreId())
      .setFreightPrice(unit.getTotalFreight()).setFinalTotalPrice(unit.getTotalPrice());
  }

  private Mono<?> checkRpcMessage(ConfirmOrderMessage message) {
    return Optional.ofNullable(message)
      .map(e -> message.getOrderPreview())
      .map(e -> message.getDeliveryAddress())
      .map(Mono::just)
      .orElse(Mono.error(ExceptionUtils.invalidParam("Invalid trade")));
  }

  private Mono<SubmitOrderResult> startPayInternal(/*@NonNull*/ Long paymentId) {
    return paymentService.startPayment(paymentId)
      .map(e -> new SubmitOrderResult().setPaymentData(e));
  }

  @Override
  public Mono<SubmitOrderResult> startPayForOrder(Long orderId) {
    if (Objects.isNull(orderId) || orderId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("orderId"));
    }
    return orderService.getOrderMetadataById(orderId)
      .flatMap(e -> startPayInternal(e.getPaymentId()));
  }

  @Override
  public Mono<PaymentResult> cancelOrder(String proposer, Long orderId) {
    if (Objects.isNull(orderId) || orderId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("orderId"));
    }
    return orderService.getOrderMetadataById(orderId)
      .flatMap(v -> compensateInventory(orderId))
      .flatMap(v -> orderStatusService.cancelOrder(proposer, orderId))
      .map(e -> new PaymentResult());
    /*return orderService.getOrderMetadataById(orderId)
      .flatMap(order -> paymentService.cancelPay(proposer, order.getPaymentId()))
      .flatMap(result -> compensateInventory(orderId)
        .flatMap(v -> orderStatusService.cancelOrder(proposer, orderId))
        .map(e -> result)
      );*/
  }

  private Mono<Long> compensateInventory(long orderId) {
    return asyncExecute(() -> recoverInventoryInternal(orderId));
  }

  @Transactional
  protected void recoverInventoryInternal(long orderId) {
    List<OrderProduct> orderProducts = orderProductRepository.getByOrderId(orderId).stream()
      .filter(e -> e.getStatus().equals(0)) // Filter the available products
      .collect(Collectors.toList());
    for (OrderProduct product : orderProducts) {
      inventoryRepository.addAmount(product.getId(), product.getAmount());
    }
  }
}
