package io.spm.parknshop.api.controller.payment;

import io.spm.parknshop.advertisement.service.AdvertisementWorkflowService;
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
  @Autowired
  private AdvertisementWorkflowService advertisementWorkflowService;

  @ResponseBody
  @GetMapping("/buy/notify_callback")
  public Mono<?> buyPayNotifyCallback(ServerWebExchange exchange, @RequestParam("trade_no") String outerPaymentId,
                                      @RequestParam("out_trade_no") String shopPaymentId) {
    exchange.getResponse().getHeaders().add(HttpHeaders.LOCATION, "http://localhost:4010/#/buy/finish_buy?tradeId=" + shopPaymentId);
    exchange.getResponse().setStatusCode(HttpStatus.valueOf(302));
    return paymentService.finishPay(shopPaymentId, outerPaymentId);
  }

  @ResponseBody
  @GetMapping("/ad/notify_callback")
  public Mono<?> adPayNotifyCallback(ServerWebExchange exchange, @RequestParam("trade_no") String outerPaymentId,
                                     @RequestParam("out_trade_no") String adPaymentId) {

    return advertisementWorkflowService.finishPay(adPaymentId, outerPaymentId)
      .map(advertisement -> {
        exchange.getResponse().getHeaders().add(HttpHeaders.LOCATION, "http://localhost:4012/#/ad/finish_pay?tradeId=" + outerPaymentId + "&adId=" + advertisement.getId());
        exchange.getResponse().setStatusCode(HttpStatus.valueOf(302));
        return advertisement;
      });
  }

  @ResponseBody
  @PostMapping("/return_callback")
  public Mono<?> payReturnCallback(ServerWebExchange exchange, @RequestParam("trade_no") String outerPaymentId,
                                   @RequestParam("out_trade_no") String shopPayment) {
    return Mono.just("ok");
  }
}
