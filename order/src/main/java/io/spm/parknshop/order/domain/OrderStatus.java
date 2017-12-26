package io.spm.parknshop.order.domain;

/**
 * @author Eric Zhao
 */
public final class OrderStatus {

  public static final int UNEXPECTED_STATE = -1;

  public static final int NEW_CREATED = 0;
  public static final int PAYED = 1;
  public static final int PREPARING_SHIPMENT = 2;
  public static final int SHIPPED = 3;
  public static final int DELIVERED = 4;
  public static final int COMPLETED = 5;
  public static final int COMMENTED = 6;
  public static final int CANCELED = 9;

  private OrderStatus() {}
}
