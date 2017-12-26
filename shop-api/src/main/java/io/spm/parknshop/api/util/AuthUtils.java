package io.spm.parknshop.api.util;

import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public final class AuthUtils {

  public static final String PRINCIPAL_HEADER_INTERNAL = "Shop-Principal-K";

  public static final String USER_PREFIX = "USER_";
  public static final String SELLER_PREFIX = "SELLER_";
  public static final String ADMIN_PREFIX = "ADMIN_";

  private static String extractPrincipal(ServerWebExchange exchange) {
    return exchange.getResponse().getHeaders().getFirst(PRINCIPAL_HEADER_INTERNAL);
  }

  public static Mono<String> getNonAdminPrincipal(ServerWebExchange exchange) {
    String principal = extractPrincipal(exchange);
    if (StringUtils.isEmpty(principal) || (!principal.startsWith(USER_PREFIX) && !principal.startsWith(SELLER_PREFIX))) {
      return Mono.error(new ServiceException(ErrorConstants.USER_ROLE_NO_PERMISSION, "Your role doesn't have the permission"));
    }
    return Mono.just(principal);
  }

  public static Mono<Long> getUserId(ServerWebExchange exchange) {
    String principal = extractPrincipal(exchange);
    if (StringUtils.isEmpty(principal) || !principal.startsWith(USER_PREFIX)) {
      return Mono.error(new ServiceException(ErrorConstants.USER_ROLE_NO_PERMISSION, "Your role doesn't have the permission"));
    }
    return Mono.just(principal.replace(USER_PREFIX, ""))
      .map(Long::valueOf);
  }

  public static Mono<Long> getSellerId(ServerWebExchange exchange) {
    String principal = extractPrincipal(exchange);
    if (StringUtils.isEmpty(principal) || !principal.startsWith(SELLER_PREFIX)) {
      return Mono.error(new ServiceException(ErrorConstants.USER_ROLE_NO_PERMISSION, "Your role doesn't have the permission"));
    }
    return Mono.just(principal.replace(SELLER_PREFIX, ""))
      .map(Long::valueOf);
  }

  public static Mono<Long> getAdminId(ServerWebExchange exchange) {
    String principal = extractPrincipal(exchange);
    if (StringUtils.isEmpty(principal) || !principal.startsWith(ADMIN_PREFIX)) {
      return Mono.error(new ServiceException(ErrorConstants.NO_AUTH, "Your role doesn't have the permission"));
    }
    return Mono.just(principal.replace(ADMIN_PREFIX, ""))
      .map(Long::valueOf);
  }

  private AuthUtils() {}
}
