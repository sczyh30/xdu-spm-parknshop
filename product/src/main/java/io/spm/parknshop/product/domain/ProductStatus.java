package io.spm.parknshop.product.domain;

/**
 * @author Eric Zhao
 */
public final class ProductStatus {

  public static final int NORMAL = 0;
  public static final int NOT_AVAILABLE = 1;
  public static final int REMOVED = 4;

  public static boolean isAvailable(int status) {
    return status == NORMAL;
  }

  public static boolean isRemoved(int status) {
    return status == REMOVED;
  }

  private ProductStatus() {}
}
