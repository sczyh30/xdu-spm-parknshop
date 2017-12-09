package io.spm.parknshop.api.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.spm.parknshop.api.common.WritableResponseSupport;
import io.spm.parknshop.api.util.Result;
import io.spm.parknshop.common.auth.AuthPrincipal;
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
      return chain.filter(exchange);
    }
    return extractFromHeader(exchange.getRequest().getHeaders())
      .flatMap(this::verifyAndExtractPrincipal)
      .flatMap(principal -> verifyRole(principal, exchange, path))
      .flatMap(v -> chain.filter(exchange))
      .onErrorResume(ex -> handleException(ex, exchange));
  }

  private boolean testWithoutAuthentication(/*@NonNull*/ String path) {
    if (path.startsWith("/api/v1/user/login") || path.startsWith("/api/v1/user/register") || path.startsWith("/api/v1/product")) {
      return true;
    }
    return false;
  }

  private boolean isAuthException(ServiceException ex) {
    return ex.getErrorCode() == ErrorConstants.NO_AUTH || ex.getErrorCode() == ErrorConstants.USER_INVALID_TOKEN;
  }

  private Mono<Void> handleException(Throwable ex, ServerWebExchange exchange) {
    logger.error("Authentication failed", ex);
    if (ex instanceof ServiceException) {
      if (isAuthException((ServiceException) ex)) {
        return ServerResponse.status(HttpStatus.UNAUTHORIZED)
          .contentType(MediaType.APPLICATION_JSON_UTF8)
          .body(BodyInserters.fromObject(Result.unauthorized()))
          .flatMap(response -> writeTo(response, exchange));
      }
    }
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
      .body(BodyInserters.fromObject(Result.unknownError()))
      .flatMap(response -> writeTo(response, exchange));
  }

  private Mono<Long> verifyRole(/*@NonNull*/ AuthPrincipal principal, ServerWebExchange exchange, String path) {
    return roleAuthenticationDecider.verifyRole(principal, exchange, path);
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
    return new AuthPrincipal().setId(decodedJWT.getClaim("id").asString())
      .setUsername(decodedJWT.getClaim("username").asString())
      .setRole(decodedJWT.getClaim("role").asInt())
      .setExpireDate(decodedJWT.getExpiresAt());
  }

  private boolean isApiRoute(String path) {
    return !StringUtils.isEmpty(path) && path.startsWith("/api/");
  }
}
