package io.spm.parknshop.common.async;

import io.spm.parknshop.common.concurrent.NiceThreadFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Wrapper from blocking functions to async future. Adapt to Spring Reactor.
 *
 * @author Eric Zhao
 */
public final class ReactorAsyncWrapper {

  private static final int poolSize = 64;

  static final ExecutorService pool = Executors.newFixedThreadPool(poolSize,
    new NiceThreadFactory("reactive-jdbc-blocking-pool"));
  private static final Scheduler scheduler = Schedulers.fromExecutor(pool);

  /**
   * Wrap a blocking callable into a {@link Mono} with provided blocking worker pool.
   *
   * @param f blocking function
   * @param <R> type of async result
   * @return wrapped async result
   */
  public static <R> Mono<R> async(Callable<R> f) {
    return Mono.fromCallable(f).subscribeOn(scheduler);
  }

  public static Mono<Long> asyncExecute(Runnable f) {
    return Mono.fromRunnable(f).subscribeOn(scheduler).map(e -> 0L).switchIfEmpty(Mono.just(0L));
  }

  public static <R> Flux<R> asyncIterable(Callable<Iterable<R>> f) {
    return Mono.fromCallable(f)
      .subscribeOn(scheduler)
      .flatMapIterable(e -> e);
  }

  private ReactorAsyncWrapper() {}
}
