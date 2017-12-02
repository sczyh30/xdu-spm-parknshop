package io.spm.parknshop.common.async;

import io.spm.parknshop.common.concurrent.NiceThreadFactory;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Wrapper from blocking functions to async future.
 *
 * @author Eric Zhao 14130140389
 * @date 2017/12/01
 */
public final class RxAsyncWrapper {

  private static final int poolSize = 64;

  private static final ExecutorService pool = Executors.newFixedThreadPool(poolSize,
    new NiceThreadFactory("rx-jdbc-blocking-pool"));
  private static final Scheduler scheduler = Schedulers.from(pool);

  /**
   * Wrap a blocking callable into a {@link Single} with provided blocking worker pool.
   *
   * @param f blocking function
   * @param <R> type of async result
   * @return wrapped async result
   */
  public static <R> Single<R> async(Callable<R> f) {
    return Single.fromCallable(f).subscribeOn(scheduler);
  }

  public static <R> Observable<R> asyncIterable(Callable<Iterable<R>> f) {
    return Observable.fromCallable(f)
      .subscribeOn(scheduler)
      .flatMapIterable(e -> e);
  }

  private RxAsyncWrapper() {}
}
