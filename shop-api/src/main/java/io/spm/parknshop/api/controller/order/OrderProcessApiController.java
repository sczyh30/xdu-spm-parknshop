package io.spm.parknshop.api.controller.order;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.order.service.OrderStatusService;
import io.spm.parknshop.trade.domain.PaymentResult;
import io.spm.parknshop.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class OrderProcessApiController {

  @Autowired
  private OrderStatusService orderStatusService;
  @Autowired
  private TradeService tradeService;

  @PostMapping("/order/op/prepare_shipment/{id}")
  public Mono<Long> apiPrepareShipment(ServerWebExchange exchange, @PathVariable("id") Long id) {
    return AuthUtils.getSellerId(exchange)
      .flatMap(sellerId -> orderStatusService.prepareShipping(id));
  }

  @PostMapping("/order/op/finish_shipment/{id}")
  public Mono<Long> apiFinishShipment(ServerWebExchange exchange, @PathVariable("id") Long id) {
    return AuthUtils.getSellerId(exchange)
      .flatMap(sellerId -> orderStatusService.finishShipping(id));
  }

  @PostMapping("/order/op/finish_delivery/{id}")
  public Mono<Long> apiFinishDelivery(ServerWebExchange exchange, @PathVariable("id") Long id) {
    return AuthUtils.getSellerId(exchange)
      .flatMap(sellerId -> orderStatusService.finishDelivery(id));
  }

  @PostMapping("/order/op/complete/{id}")
  public Mono<Long> apiConfirmDeliveryAndCompleteOrder(ServerWebExchange exchange, @PathVariable("id") Long id) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> orderStatusService.confirmAndComplete(AuthUtils.USER_PREFIX + userId, id));
  }

  @PostMapping("/order/op/cancel/{id}")
  public Mono<PaymentResult> apiCancelOrder(ServerWebExchange exchange, @PathVariable("id") Long id) {
    return AuthUtils.getNonAdminPrincipal(exchange)
      .flatMap(proposer -> tradeService.cancelOrder(proposer, id));
  }
}
