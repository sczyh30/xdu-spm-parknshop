package io.spm.parknshop.api.controller.admin;

import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.service.ApplyProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
public class AdminWorkflowApiController {

  @Autowired
  @Qualifier("adApplyService")
  private ApplyProcessService adApplyProcessService;

  @PostMapping("/admin/manage/apply_workflow/ad/reject/{applyId}")
  public Mono<Long> apiRejectAdApply(ServerWebExchange exchange, @PathVariable("applyId") Long applyId,
                                @RequestBody ApplyResult applyResult) {
    return AuthUtils.getAdminId(exchange)
      .flatMap(adminId -> adApplyProcessService.rejectApply(applyId, AuthUtils.ADMIN_PREFIX + adminId, applyResult));
  }

  @PostMapping("/admin/manage/apply_workflow/ad/approve/{applyId}")
  public Mono<Long> apiApproveAdApply(ServerWebExchange exchange, @PathVariable("applyId") Long applyId,
                                @RequestBody ApplyResult applyResult) {
    return AuthUtils.getAdminId(exchange)
      .flatMap(adminId -> adApplyProcessService.approveApply(applyId, AuthUtils.ADMIN_PREFIX + adminId, applyResult));
  }
}
