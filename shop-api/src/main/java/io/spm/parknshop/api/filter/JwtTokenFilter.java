package io.spm.parknshop.api.filter;

import io.spm.parknshop.api.util.Result;
import io.spm.parknshop.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Route filter for JWT authentication.
 *
 * @author Eric Zhao
 */
public class JwtTokenFilter extends WritableResponseSupport implements WebFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

  @Autowired
  private UserService userService;

  public JwtTokenFilter(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
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
    return chain.filter(exchange);
    // TODO: implement this!
    /*return ServerResponse.status(HttpStatus.UNAUTHORIZED)
      .body(BodyInserters.fromObject(Result.unauthorized()))
      .flatMap(response -> writeTo(response, exchange));*/
  }

  private String extractFromHeader(HttpHeaders headers) {
    return headers.get("").stream().collect(Collectors.joining());
  }

  private boolean isApiRoute(String path) {
    return !StringUtils.isEmpty(path) && path.startsWith("/api/");
  }
}
