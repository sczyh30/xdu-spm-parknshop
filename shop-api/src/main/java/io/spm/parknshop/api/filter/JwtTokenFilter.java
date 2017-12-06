package io.spm.parknshop.api.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.spm.parknshop.api.util.Result;
import io.spm.parknshop.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Route filter for JWT authentication.
 *
 * @author Eric Zhao
 */
// @Component
public class JwtTokenFilter implements WebFilter {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private UserService userService;

  private DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String path = exchange.getRequest().getPath().pathWithinApplication().value();
    if (isApiRoute(path)) {
      return verifyAuthentication(exchange, path, chain);
    }
    return chain.filter(exchange);
  }

  private Mono<Void> verifyAuthentication(ServerWebExchange exchange, String path, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();

    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    return Mono.empty();
  }

  private String extractFromHeader(HttpHeaders headers) {
    return headers.get("").stream().collect(Collectors.joining());
  }

  private Mono<DataBuffer> createUnauthorizedResult() {
    try {
      byte[] bytes = gson.toJson(Result.unauthorized()).getBytes("UTF-8");
      return Mono.just(dataBufferFactory.allocateBuffer().write(bytes));
    } catch (Exception ex) {
      return Mono.empty();
    }
  }

  private boolean isApiRoute(String path) {
    return !StringUtils.isEmpty(path) && path.startsWith("/api/");
  }
}
