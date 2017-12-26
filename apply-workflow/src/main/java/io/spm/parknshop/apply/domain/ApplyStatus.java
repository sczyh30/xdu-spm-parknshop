package io.spm.parknshop.apply.domain;

public final class ApplyStatus {

  public static final int NEW_APPLY = 0;
  public static final int APPROVED = 1;
  public static final int REJECTED = 2;
  public static final int CANCELED = 3;

  public static boolean canEdit(int status) {
    return status == NEW_APPLY || status == REJECTED || status == CANCELED;
  }
}
