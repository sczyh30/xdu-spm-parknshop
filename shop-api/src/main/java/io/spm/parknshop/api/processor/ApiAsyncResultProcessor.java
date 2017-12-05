package io.spm.parknshop.api.processor;

import io.spm.parknshop.api.util.Result;
import io.spm.parknshop.common.exception.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.RxReactiveStreams;
import rx.Single;

import java.lang.reflect.Method;
import java.util.List;

import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * Captures an API route handler function. Process the async result
 * and wrap with the REST {@link Result}.
 * Current support RxJava 1.x and Spring Reactor.
 *
 * @author Eric Zhao
 */
@Aspect
@Component
public class ApiAsyncResultProcessor {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Around(("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))"))
  public Object processAsyncHandler(ProceedingJoinPoint pjp) {
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    Method asyncMethod = methodSignature.getMethod();
    try {
      return processAsyncResult(pjp.proceed());
    } catch (Throwable ex) {
      logger.error("Unexpected error in handler function {}", asyncMethod.getName(), ex);
      return newUnknownError(asyncMethod.getReturnType());
    }
  }

  private Object processAsyncResult(Object asyncResult) {
    // TODO: Current we cannot modify the return type in terms of dynamic proxy pattern. Need a more flexible mechanism.
    Class<?> asyncClass = asyncResult.getClass();
    // Dispatch the type async result and apply for processors.
    if (Mono.class.isAssignableFrom(asyncClass)) {
      return processReactorMono((Mono<?>) asyncResult);
    } else if (Flux.class.isAssignableFrom(asyncClass)) {
      return processReactorFlux((Flux<?>) asyncResult);
    } else if (Single.class.isAssignableFrom(asyncClass)) {
      return processRxSingle((Single<?>) asyncResult);
    } else if (Observable.class.isAssignableFrom(asyncClass)) {
      return processRxObservable((Observable<?>) asyncResult);
    } else {
      // Can't recognize the type of result, directly return the original result.
      return asyncResult;
    }
  }

  private <R> Mono<Result<R>> processReactorMono(Mono<R> result) {
    return result.map(Result::success)
      .onErrorResume(this::handleReactorServiceFallback)
      .switchIfEmpty(Mono.just(Result.notFound()));
  }

  private <R> Mono<Result<List<R>>> processReactorFlux(Flux<R> result) {
    return result.collectList()
      .map(Result::success)
      .onErrorResume(this::handleReactorServiceFallback)
      .switchIfEmpty(Mono.just(Result.notFound()));
  }

  private <R> Single<Result<R>> processRxSingle(Single<R> result) {
    return result.map(Result::successIfPresent)
      .onErrorResumeNext(this::handleRxServiceFallback);
  }

  /**
   * Note: Here we use {@link Publisher} to adapt to the type of async result.
   */
  private <R> Publisher<Result<List<R>>> processRxObservable(Observable<R> result) {
    Single<Result<List<R>>> single = result.toList()
      .toSingle()
      .map(Result::success)
      .onErrorResumeNext(this::handleRxServiceFallback);
    return RxReactiveStreams.toPublisher(single);
  }

  private void logError(Throwable ex) {
    if (ex instanceof ServiceException) {
      logger.error("Service error", ex);
    } else {
      logger.error("Unexpected error", ex);
    }
  }

  /**
   * Provides fallback result for service error.
   *
   * @param ex service exception
   * @return wrapped fallback result
   */
  private <R> Mono<Result<R>> handleReactorServiceFallback(Throwable ex) {
    logError(ex);
    if (ex instanceof ServiceException) {
      return Mono.just(Result.failure(((ServiceException) ex).getErrorCode(), ex));
    }
    return Mono.just(Result.failure(INTERNAL_UNKNOWN_ERROR, "unknown_error"));
  }

  private <R> Single<Result<R>> handleRxServiceFallback(Throwable ex) {
    logError(ex);
    if (ex instanceof ServiceException) {
      return Single.just(Result.failure(((ServiceException) ex).getErrorCode(), ex));
    }
    return Single.just(Result.failure(INTERNAL_UNKNOWN_ERROR, "unknown_error"));
  }

  private <R> Object newUnknownError(Class<?> asyncClass) {
    Result<R> failedResult = Result.failure(INTERNAL_UNKNOWN_ERROR, "unknown_error");
    if (Mono.class.isAssignableFrom(asyncClass) || Flux.class.isAssignableFrom(asyncClass)) {
      return Mono.just(failedResult);
    } else if (Single.class.isAssignableFrom(asyncClass)) {
      return Single.just(failedResult);
    } else if (Observable.class.isAssignableFrom(asyncClass)) {
      return RxReactiveStreams.toPublisher(Single.just(failedResult));
    } else {
      // Can't recognize the type of result, return wrapped Mono by default.
      return Mono.just(failedResult);
    }
  }
}
