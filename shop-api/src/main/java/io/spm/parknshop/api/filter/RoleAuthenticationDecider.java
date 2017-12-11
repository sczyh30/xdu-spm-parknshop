package io.spm.parknshop.api.filter;

import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.common.auth.AuthPrincipal;
import io.spm.parknshop.common.auth.AuthRoles;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
class RoleAuthenticationDecider {

  @Autowired
  private UserService userService;
  @Autowired
  private AdminService adminService;

  Mono<Long> verifyRole(/*@NonNull*/ AuthPrincipal principal, ServerWebExchange exchange, String path) {
    // TODO: implement this.
    switch (principal.getRole()) {
      case AuthRoles.CUSTOMER:
      case AuthRoles.SELLER:
        if (path.startsWith("/api/v1/admin/") || path.startsWith("/api/v1/user/register")) {
          return Mono.error(ExceptionUtils.authNoPermission());
        } else {
          return Mono.just(0L);
        }
      case AuthRoles.ADMIN:
        return Mono.just(0L);
      default:
        return Mono.error(ExceptionUtils.authNoPermission());
    }
  }
}
