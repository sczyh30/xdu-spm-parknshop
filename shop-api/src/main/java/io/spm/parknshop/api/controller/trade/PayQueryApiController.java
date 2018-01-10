package io.spm.parknshop.api.controller.trade;

import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class PayQueryApiController {

  @Autowired
  private PaymentService paymentService;

  @GetMapping("/payment/record/{id}")
  public Mono<PaymentRecord> apiGetById(@PathVariable("id") String id) {
    return paymentService.getPaymentById(id);
  }
}
