package io.spm.parknshop.refund.domain;

/**
 * @author Eric Zhao
 */
public final class RefundStatus {

  public static final int NEW_CREATED = 0;
  public static final int APPROVED_WAIT_RETURN = 1;
  public static final int PENDING_REFUND_TRANSACTION = 2;
  public static final int REFUND_SUCCESSFUL = 3;
  public static final int REFUND_UNEXPECTED_FAILED = 4;
  public static final int REJECTED = 7;
  public static final int CANCELED = 9;

  private RefundStatus() {}
}
