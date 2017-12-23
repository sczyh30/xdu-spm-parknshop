package io.spm.parknshop.api.controller;

import io.spm.parknshop.buy.domain.ConfirmOrderDO;
import io.spm.parknshop.buy.service.ConfirmOrderService;
import io.spm.parknshop.trade.domain.ConfirmOrderResult;
import io.spm.parknshop.trade.domain.OrderPreview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
public class BuyApiController {

  @Autowired
  private ConfirmOrderService confirmOrderService;

  @GetMapping("/buy/render_order_preview")
  public Mono<OrderPreview> apiRenderOrderPreview(ServerWebExchange exchange) {
    return confirmOrderService.previewOrder(1L);
  }

  @PostMapping("/buy/confirm_order")
  public Mono<ConfirmOrderResult> apiConfirmOrder(ServerWebExchange exchange, @RequestBody ConfirmOrderDO confirmOrderDO) {
    return confirmOrderService.confirmOrder(1L, confirmOrderDO);
  }
}
