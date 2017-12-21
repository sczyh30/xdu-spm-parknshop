package io.spm.parknshop.advertisement.domain;

/**
 * @author Eric Zhao
 */
public final class AdStatus {

  /**
   * Apply status.
   */
  public static final int PAYED = 10;

  /**
   * Expand from `APPROVED` status.
   */
  public static final int IN_EXHIBITION = 11;
  public static final int FINISH = 12;

  private AdStatus() {}
}
