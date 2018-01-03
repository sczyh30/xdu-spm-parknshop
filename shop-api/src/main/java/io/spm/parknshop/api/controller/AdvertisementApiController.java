package io.spm.parknshop.api.controller;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.advertisement.domain.apply.AdvertisementDTO;
import io.spm.parknshop.apply.domain.ApplyProcessorRoles;
import io.spm.parknshop.apply.service.ApplyService;
import io.spm.parknshop.query.service.impl.AdApplyRenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author four
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class AdvertisementApiController {

  @Autowired
  private ApplyService<AdvertisementDTO, Long> applyService;
  @Autowired
  private AdApplyRenderService adApplyRenderService;

  @PostMapping("/ad/submit_apply")
  public Mono<Long> apiAdApply(ServerWebExchange exchange, @RequestBody AdvertisementDTO advertisement){
    return AuthUtils.getSellerId(exchange)
      .flatMap(sellerId -> applyService.applyFor(ApplyProcessorRoles.SELLER_PREFIX + sellerId, advertisement));
  }

  @GetMapping("/ad/render_new_apply")
  public Mono<?> apiRenderNewApply(ServerWebExchange exchange) {
    return AuthUtils.getSellerId(exchange)
      .flatMap(adApplyRenderService::renderNewApplyFor);
  }
}
