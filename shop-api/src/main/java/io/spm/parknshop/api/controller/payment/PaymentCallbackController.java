package io.spm.parknshop.api.controller.payment;

import io.spm.parknshop.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/pay")
public class PaymentCallbackController {

  @Autowired
  private PaymentService paymentService;

  @ResponseBody
  @GetMapping("/notify_callback")
  public Mono<?> payNotifyCallback(ServerWebExchange exchange,@RequestParam("trade_no") String outerPaymentId,
                                   @RequestParam("out_trade_no") String shopPayment) {
    exchange.getResponse().getHeaders().add(HttpHeaders.LOCATION, "http://localhost:4010/#/");
    exchange.getResponse().setStatusCode(HttpStatus.valueOf(302));
    Long shopPaymentId = Long.valueOf(shopPayment);
    return paymentService.finishPay(shopPaymentId, outerPaymentId);

  }

  @ResponseBody
  @PostMapping("/return_callback")
  public Mono<?> payReturnCallback(ServerWebExchange exchange, @RequestParam("trade_no") String outerPaymentId,
                                   @RequestParam("out_trade_no") String shopPayment) {
    return Mono.just("ok");
  }
}
