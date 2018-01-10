package io.spm.parknshop.api.controller.trade;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.buy.domain.ConfirmOrderDO;
import io.spm.parknshop.buy.service.ConfirmOrderService;
import io.spm.parknshop.trade.domain.SubmitOrderResult;
import io.spm.parknshop.trade.domain.OrderPreview;
import io.spm.parknshop.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
public class BuyApiController {

  @Autowired
  private ConfirmOrderService confirmOrderService;
  @Autowired
  private TradeService tradeService;

  @GetMapping("/buy/render_order_preview")
  public Mono<OrderPreview> apiRenderOrderPreview(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMap(confirmOrderService::previewOrder);
  }

  @PostMapping("/buy/confirm_order")
  public Mono<SubmitOrderResult> apiConfirmOrder(ServerWebExchange exchange, @RequestBody ConfirmOrderDO confirmOrderDO) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> confirmOrderService.submitOrder(userId, confirmOrderDO));
  }

  @PostMapping("/buy/order_pay")
  public Mono<? extends SubmitOrderResult> apiContinuePayForOrder(ServerWebExchange exchange, @RequestParam("order") Long id) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> tradeService.startPayForOrder(id));
  }
}
