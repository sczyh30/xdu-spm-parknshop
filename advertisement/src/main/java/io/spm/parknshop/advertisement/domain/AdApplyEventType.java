package io.spm.parknshop.advertisement.domain;

/**
 * @author Eric Zhao
 */
public final class AdApplyEventType {

  public static final int SUBMIT_APPLY = 1;
  public static final int APPROVE_APPLY = 2;
  public static final int FINISH_PAY = 3;
  public static final int REJECT_APPLY = 4;
  public static final int WITHDRAW_APPLY = 5;

  private AdApplyEventType() {}
}
