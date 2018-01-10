package io.spm.parknshop.api.controller.ad;

import io.spm.parknshop.advertisement.service.AdvertisementWorkflowService;
import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.trade.domain.PaymentRedirectData;
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
public class AdWorkflowApiController {

  @Autowired
  private AdvertisementWorkflowService advertisementWorkflowService;

  @PostMapping("/ad/workflow/start_pay/{id}")
  public Mono<? extends PaymentRedirectData> apiStartPay(@PathVariable("id") Long id, ServerWebExchange exchange) {
    return advertisementWorkflowService.startPay(id);
    //return AuthUtils.getSellerId(exchange)
     // .flatMap(sellerId -> advertisementWorkflowService.startPay(id));
  }
}
