package io.spm.parknshop.advertisement.domain;

/**
 * @author Eric Zhao
 */
public final class AdStatus {

  /**
   * Apply status.
   */
  public static final int NEW_APPLY = 0;
  public static final int APPROVED = 1; // Pending payment
  public static final int PAYED = 2;
  public static final int REJECTED = 3;
  public static final int CANCELED = 4;
  // public static final int PAYMENT_TIMEOUT = 31;

  /**
   * Expand from `APPROVED` status.
   */
  public static final int IN_EXHIBITION = 21;
  public static final int FINISH = 22;

  public static boolean canEdit(int status) {
    return status == NEW_APPLY || status == REJECTED || status == CANCELED;
  }

  private AdStatus() {}
}
