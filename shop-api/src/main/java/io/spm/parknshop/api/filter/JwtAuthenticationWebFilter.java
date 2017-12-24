package io.spm.parknshop.api.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.spm.parknshop.api.common.WritableResponseSupport;
import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.api.util.Result;
import io.spm.parknshop.api.util.ResultUtils;
import io.spm.parknshop.common.auth.AuthPrincipal;
import io.spm.parknshop.common.auth.AuthRole;
import io.spm.parknshop.common.auth.AuthRoles;
import io.spm.parknshop.common.auth.JWTUtils;
import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * Route filter for JWT authentication.
 *
 * @author Eric Zhao
 */
public class JwtAuthenticationWebFilter extends WritableResponseSupport implements WebFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationWebFilter.class);

  private static final String AUTHENTICATION_HEADER = "Authorization";
  private static final String AUTH_HEADER_PREFIX = "Bearer ";

  @Autowired
  private RoleAuthenticationDecider roleAuthenticationDecider;

  public JwtAuthenticationWebFilter(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
    super(viewResolvers, serverCodecConfigurer);
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String path = exchange.getRequest().getPath().pathWithinApplication().value();
    if (isApiRoute(path)) {
      return verifyAuthentication(exchange, path, chain);
    }
    return chain.filter(exchange);
  }

  private Mono<Void> verifyAuthentication(ServerWebExchange exchange, String path, WebFilterChain chain) {
    if (testWithoutAuthentication(path)) {
      // TODO: temporary solution!
      return extractFromHeader(exchange.getRequest().getHeaders())
        .flatMap(this::verifyAndExtractPrincipal)
        .flatMap(principal -> verifyRole(principal, exchange, path))
        .flatMap(v -> chain.filter(exchange))
        .onErrorResume(v -> chain.filter(exchange));
    }
    return extractFromHeader(exchange.getRequest().getHeaders())
      .flatMap(this::verifyAndExtractPrincipal)
      .flatMap(principal -> verifyRole(principal, exchange, path))
      .onErrorResume(ex -> handleAuthException(ex, exchange).map(e -> 0L))
      .flatMap(v -> chain.filter(exchange));
  }

  private boolean isAuthException(ServiceException ex) {
    return ex.getErrorCode() == ErrorConstants.NO_AUTH || ex.getErrorCode() == ErrorConstants.USER_INVALID_TOKEN;
  }

  private Mono<Void> handleAuthException(Throwable ex, ServerWebExchange exchange) {
    logger.error("Authentication failed", ex);
    if (ex instanceof ServiceException) {
      if (isAuthException((ServiceException) ex)) {
        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
          .contentType(MediaType.APPLICATION_JSON_UTF8)
          .body(BodyInserters.fromObject(Result.unauthorized()))
          .flatMap(response -> writeTo(response, exchange));
      }
    }
    Result<?> result = ResultUtils.toApiResult(ex);
    return ServerResponse.status(ResultUtils.getStatus(result)).contentType(MediaType.APPLICATION_JSON_UTF8)
      .body(BodyInserters.fromObject(result))
      .flatMap(response -> writeTo(response, exchange));
  }

  private Mono<Long> verifyRole(/*@NonNull*/ AuthPrincipal principal, ServerWebExchange exchange, String path) {
    addInternalHeader(principal, exchange);
    return roleAuthenticationDecider.verifyRole(principal, exchange, path);
  }

  private void addInternalHeader(AuthPrincipal principal, ServerWebExchange exchange) {
    exchange.getResponse().getHeaders().add(AuthUtils.PRINCIPAL_HEADER_INTERNAL, wrapPrincipalHeader(principal));
  }

  private String wrapPrincipalHeader(AuthPrincipal principal) {
    switch (principal.getRole()) {
      case AuthRoles.SELLER:
        return AuthUtils.SELLER_PREFIX + principal.getId();
      case AuthRoles.ADMIN:
        return AuthUtils.ADMIN_PREFIX + principal.getId();
      case AuthRoles.CUSTOMER:
        return AuthUtils.USER_PREFIX + principal.getId();
      default:
        return "UNKNOWN";
    }
  }

  private Mono<String> extractFromHeader(HttpHeaders headers) {
    return Optional.ofNullable(headers.getFirst(AUTHENTICATION_HEADER))
      .filter(header -> header.startsWith(AUTH_HEADER_PREFIX))
      .map(header -> header.substring(AUTH_HEADER_PREFIX.length(), header.length()))
      .map(Mono::just)
      .orElse(Mono.error(ExceptionUtils.invalidToken()));
  }

  private Mono<AuthPrincipal> verifyAndExtractPrincipal(String token) {
    return JWTUtils.verifyToken(token)
      .map(this::fromJwtToken);
  }

  private AuthPrincipal fromJwtToken(DecodedJWT decodedJWT) {
    return new AuthPrincipal().setId(decodedJWT.getClaim("id").asInt().toString())
      .setUsername(decodedJWT.getClaim("username").asString())
      .setRole(decodedJWT.getClaim("role").asInt())
      .setExpireDate(decodedJWT.getExpiresAt());
  }

  private boolean isApiRoute(String path) {
    return !StringUtils.isEmpty(path) && path.startsWith("/api/");
  }

  private boolean testWithoutAuthentication(/*@NonNull*/ String path) {
    if (path.startsWith("/api/v1/user/login") || path.startsWith("/api/v1/user/register") || path.startsWith("/api/v1/search") || path.startsWith("/api/v1/store") || path.contains("delivery")
      || path.startsWith("/api/v1/product") || path.startsWith("/api/v1/catalog") || path.startsWith("/api/v1/index") || path.startsWith("/api/v1/admin/login")) {
      return true;
    }
    return false;
  }
}
