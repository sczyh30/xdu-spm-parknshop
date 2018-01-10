package io.spm.parknshop.api.filter;

import io.spm.parknshop.api.util.Result;
import io.spm.parknshop.api.util.ResultUtils;
import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

/**
 * Reactive exception handler for service exceptions and other common exceptions.
 *
 * @author Eric Zhao
 */
@Order(-2)
public class ApiErrorExceptionHandler extends AbstractErrorWebExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(ApiErrorExceptionHandler.class);

  public ApiErrorExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ApplicationContext applicationContext) {
    super(errorAttributes, resourceProperties, applicationContext);
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.path("/api/**"), request -> handleException(request, errorAttributes));
  }

  private Mono<ServerResponse> handleException(ServerRequest request, ErrorAttributes errorAttributes) {
    return Mono.just(errorAttributes.getError(request))
      .doOnNext(this::logError)
      .map(ResultUtils::toApiResult)
      .flatMap(this::createResponse);
  }

  private <R> Mono<ServerResponse> createResponse(Result<R> result) {
    return ServerResponse.status(ResultUtils.getStatus(result))
      .contentType(MediaType.APPLICATION_JSON_UTF8)
      .body(fromObject(result));
  }

  private void logError(Throwable ex) {
    if (ex instanceof ServiceException) {
      logger.debug("Service error", ex);
    } else if (ex instanceof ResponseStatusException) {
      logger.debug("Request exception", ex);
    } else {
      logger.error("Unexpected error", ex);
    }
  }
}
