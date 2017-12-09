package io.spm.parknshop.api.common;

import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Abstract class providing support for writing responses.
 *
 * @author Eric Zhao
 */
public abstract class WritableResponseSupport {

  private List<ViewResolver> viewResolvers;

  private List<HttpMessageWriter<?>> messageWriters;

  private final ServerResponse.Context context = new ServerResponse.Context() {
    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
      return WritableResponseSupport.this.messageWriters;
    }

    @Override
    public List<ViewResolver> viewResolvers() {
      return WritableResponseSupport.this.viewResolvers;
    }
  };

  public WritableResponseSupport(List<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer) {
    this.viewResolvers = viewResolvers;
    this.messageWriters = serverCodecConfigurer.getWriters();
  }

  protected Mono<Void> writeTo(ServerResponse response, ServerWebExchange exchange) {
    return response.writeTo(exchange, context);
  }
}
