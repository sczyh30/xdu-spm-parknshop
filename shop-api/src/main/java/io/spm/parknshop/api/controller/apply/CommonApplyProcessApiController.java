package io.spm.parknshop.api.controller.apply;

import io.spm.parknshop.advertisement.domain.apply.AdApplyType;
import io.spm.parknshop.advertisement.service.AdvertisementWorkflowService;
import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.domain.ApplyVO;
import io.spm.parknshop.apply.service.ApplyDataService;
import io.spm.parknshop.apply.service.ApplyProcessService;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.query.service.ApplyQueryService;
import io.spm.parknshop.query.vo.apply.ApplyListSimpleVO;
import io.spm.parknshop.store.service.StoreWorkflowService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class CommonApplyProcessApiController {

  private final Map<Integer, ApplyProcessService> serviceMap = new ConcurrentHashMap<>();

  private final ApplyDataService applyDataService;
  private final ApplyQueryService applyQueryService;

  @Autowired
  public CommonApplyProcessApiController(AdvertisementWorkflowService adApplyService, StoreWorkflowService storeWorkflowService,
                                         ApplyDataService applyDataService, ApplyQueryService applyQueryService) {
    serviceMap.put(StoreWorkflowService.STORE_APPLY, storeWorkflowService);
    serviceMap.put(AdApplyType.APPLY_AD_PRODUCT, adApplyService);
    serviceMap.put(AdApplyType.APPLY_AD_SHOP, adApplyService);
    this.applyDataService = applyDataService;
    this.applyQueryService = applyQueryService;
  }

  @PostMapping("/workflow/apply/approve/{id}")
  public Mono<Long> apiApproveApply(@PathVariable("id") Long id, @RequestBody ApplyResult applyResult, ServerWebExchange exchange) {
    return applyDataService.getApplyById(id)
      .flatMap(apply -> findService(apply.getApplyType()))
      .flatMap(service -> service.approveApply(id, AuthUtils.extractPrincipal(exchange), applyResult));
  }

  @PostMapping("/workflow/apply/reject/{id}")
  public Mono<Long> apiRejectApply(@PathVariable("id") Long id, @RequestBody ApplyResult applyResult, ServerWebExchange exchange) {
    return applyDataService.getApplyById(id)
      .flatMap(apply -> findService(apply.getApplyType()))
      .flatMap(service -> service.rejectApply(id, AuthUtils.extractPrincipal(exchange), applyResult));
  }

  @PostMapping("/workflow/apply/cancel/{id}")
  public Mono<Long> apiCancelApply(@PathVariable("id") Long id, @RequestBody ApplyResult applyResult, ServerWebExchange exchange) {
    return applyDataService.getApplyById(id)
      .flatMap(apply -> findService(apply.getApplyType()))
      .flatMap(service -> service.cancelApply(id, AuthUtils.extractPrincipal(exchange), applyResult));
  }

  @GetMapping("/workflow/apply/render/{id}")
  public Mono<ApplyVO> apiRenderApplyView(@PathVariable("id") Long id, ServerWebExchange exchange) {
    return applyQueryService.renderApplyView(id, AuthUtils.extractPrincipal(exchange));
  }

  @GetMapping("/workflow/apply/list_u")
  public Publisher<Apply> apiApplyListForUser(ServerWebExchange exchange) {
    return AuthUtils.getNonAdminPrincipal(exchange)
      .flatMapMany(applyDataService::getApplyByProposerId);
  }

  @GetMapping("/workflow/apply/all")
  public Publisher<ApplyListSimpleVO> apiApplyListAll(ServerWebExchange exchange) {
    return AuthUtils.getAdminId(exchange)
      .flatMapMany(adminId -> applyQueryService.getAll());
  }

  private Mono<ApplyProcessService> findService(int type) {
    if (type <= 0 || !serviceMap.containsKey(type)) {
      return Mono.error(ExceptionUtils.invalidParam("apply type"));
    }
    return Mono.just(serviceMap.get(type));
  }
}
