package io.spm.parknshop.store.domain;

/**
 * @author Eric Zhao
 */
public final class StoreApplyEventType {

  public static final int SUBMIT_APPLY = 1;
  public static final int APPROVE_APPLY = 2;
  public static final int REJECT_APPLY = 3;
  public static final int WITHDRAW_APPLY = 4;

  private StoreApplyEventType() {}
}
