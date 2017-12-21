package io.spm.parknshop.api.controller;

import io.spm.parknshop.apply.domain.AdvertisementDO;
import io.spm.parknshop.apply.service.ApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author four
 */
@RestController
@RequestMapping("/api/v1/")
public class AdvertisementApiController {
  @Autowired
  ApplyService<AdvertisementDO, String> applyService;
  @PostMapping("/ad/apply")
  public Mono<?> apiAdApply(@RequestBody AdvertisementDO advertisement){
    return applyService.applyFor("1", advertisement);
  }
}
