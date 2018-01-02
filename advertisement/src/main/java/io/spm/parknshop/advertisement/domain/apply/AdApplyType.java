package io.spm.parknshop.advertisement.domain.apply;

import io.spm.parknshop.advertisement.domain.AdType;

/**
 * @author Eric Zhao
 */
public final class AdApplyType {

  private static final int UNKNOWN_APPLY_TYPE = -1;

  public static final int APPLY_AD_PRODUCT = 2;
  public static final int APPLY_AD_SHOP = 3;

  public static boolean isAdApply(int applyType) {
    return applyType == APPLY_AD_PRODUCT || applyType == APPLY_AD_SHOP;
  }

  public static int fromAdType(int adType) {
    switch (adType) {
      case AdType.AD_PRODUCT:
        return APPLY_AD_PRODUCT;
      case AdType.AD_STORE:
        return APPLY_AD_SHOP;
      default:
        return UNKNOWN_APPLY_TYPE;
    }
  }

  private AdApplyType() {}
}
