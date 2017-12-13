package io.spm.parknshop.common.async;

import rx.Completable;
import rx.Observable;
import rx.Scheduler;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.concurrent.Callable;

/**
 * Wrapper from blocking functions to async future. Adapt to RxJava 1.x.
 *
 * @author Eric Zhao
 */
public final class RxAsyncWrapper {

  private static final Scheduler scheduler = Schedulers.from(ReactorAsyncWrapper.pool);

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

  public static Completable asyncExecute(Runnable f) {
    Callable<Void> fakeCallable = () -> { f.run(); return null; };
    return Single.fromCallable(fakeCallable).subscribeOn(scheduler).toCompletable();
  }

  public static <R> Observable<R> asyncIterable(Callable<Iterable<R>> f) {
    return Observable.fromCallable(f)
      .subscribeOn(scheduler)
      .flatMapIterable(e -> e);
  }

  private RxAsyncWrapper() {}
}
