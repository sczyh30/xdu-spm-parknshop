package io.spm.parknshop.api.controller;

import io.spm.parknshop.delivery.domain.DeliveryTemplate;
import io.spm.parknshop.delivery.service.DeliveryTemplateService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class DeliveryTemplateApiController {

  @Autowired
  private DeliveryTemplateService deliveryTemplateService;

  @GetMapping("/delivery_template/by_store/{storeId}")
  public /*Flux*/ Publisher<DeliveryTemplate> apiGetProductsById(@PathVariable("storeId") Long storeId) {
    return deliveryTemplateService.getTemplateByStoreId(storeId);
  }

}
