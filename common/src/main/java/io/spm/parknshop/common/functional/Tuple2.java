package io.spm.parknshop.common.functional;

/**
 * A tuple of 2 elements.
 *
 * @author Eric Zhao
 *
 * @param <R1> type of the first element
 * @param <R2> type of the second element
 */
public class Tuple2<R1, R2> {

  public final R1 r1;
  public final R2 r2;

  private Tuple2(R1 r1, R2 r2) {
    this.r1 = r1;
    this.r2 = r2;
  }

  public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
    return new Tuple2<>(t1, t2);
  }

  public Tuple2<R2, R1> swap() {
    return new Tuple2<>(r2, r1);
  }
}
