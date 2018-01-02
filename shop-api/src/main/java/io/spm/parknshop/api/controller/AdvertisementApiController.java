package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.advertisement.domain.apply.AdvertisementDTO;
import io.spm.parknshop.apply.domain.ApplyProcessorRoles;
import io.spm.parknshop.apply.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author four
 */
@RestController
@RequestMapping("/api/v1/")
public class AdvertisementApiController {

  @Autowired
  private ApplyService<AdvertisementDTO, Long> applyService;

  @PostMapping("/ad/apply")
  public Mono<Long> apiAdApply(ServerWebExchange exchange, @RequestBody AdvertisementDTO advertisement){
    return AuthUtils.getSellerId(exchange)
      .flatMap(sellerId -> applyService.applyFor(ApplyProcessorRoles.SELLER_PREFIX + sellerId, advertisement));
  }
}
