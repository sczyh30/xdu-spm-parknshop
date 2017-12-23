package io.spm.parknshop.order.domain;

/**
 * @author Eric Zhao
 */
public final class OrderEventType {

  public static final int NEW_CREATED = 1;
  public static final int FINISH_PAY = 2;
  public static final int PROCESS_ORDER_SHIPMENT = 21;
  public static final int FINISH_SHIPMENT = 22;
  public static final int FINISH_DELIVERY = 23;
  public static final int CONFIRM_ORDER = 24;
  public static final int ADD_COMMENT = 26;
  public static final int CANCEL_ORDER = 4;

  private OrderEventType() {}
}
