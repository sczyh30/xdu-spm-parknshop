package io.spm.parknshop.trade.service.impl;

import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.Order;
import io.spm.parknshop.order.service.OrderService;
import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.payment.service.PaymentService;
import io.spm.parknshop.trade.domain.ConfirmOrderMessage;
import io.spm.parknshop.trade.domain.ConfirmOrderResult;
import io.spm.parknshop.trade.domain.OrderStoreGroupUnit;
import io.spm.parknshop.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Eric Zhao
 */
@Service
public class TradeServiceImpl implements TradeService {

  @Autowired
  private OrderService orderService;
  @Autowired
  private PaymentService paymentService;

  @Override
  public Mono<ConfirmOrderResult> dispatchAndProcessOrder(ConfirmOrderMessage confirmOrderMessage) {
    return checkRpcMessage(confirmOrderMessage)
      .flatMap(v -> paymentService.createPaymentRecord(confirmOrderMessage.getOrderPreview().getTotalPrice()))
      .flatMap(payment -> splitAndCreateOrders(confirmOrderMessage, payment)
        .flatMap(v -> paymentService.startPayment(payment.getId()))
        .map(e -> new ConfirmOrderResult().setPaymentData(e))
      );
  }

  private Mono<List<Order>> splitAndCreateOrders(ConfirmOrderMessage message, PaymentRecord paymentRecord) {
    List<Tuple2<Order, OrderStoreGroupUnit>> rawOrderList = message.getOrderPreview().getStoreGroups().stream()
      .map(e -> Tuple2.of(wrapRawOrder(e, paymentRecord, message.getDeliveryAddress().getId(), message.getCreatorId()), e))
      .collect(Collectors.toList());
    return Flux.fromIterable(rawOrderList)
      .flatMap(e -> orderService.createOrder(e.r1, e.r2))
      .collectList();
  }

  private Order wrapRawOrder(OrderStoreGroupUnit unit, PaymentRecord paymentRecord, Long addressId, Long creatorId) {
    return new Order().setCreatorId(creatorId).setAddressId(addressId)
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
}
