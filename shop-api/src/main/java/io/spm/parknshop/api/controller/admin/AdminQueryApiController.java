package io.spm.parknshop.api.controller.admin;

import io.spm.parknshop.query.service.impl.AdminDashboardQueryService;
import io.spm.parknshop.query.vo.AdminDashboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
public class AdminQueryApiController {

  @Autowired
  private AdminDashboardQueryService dashboardQueryService;

  @GetMapping("/admin/dashboard")
  public Mono<AdminDashboardVO> apiGetAdminDashboard() {
    return dashboardQueryService.getDashboardData();
  }
}
