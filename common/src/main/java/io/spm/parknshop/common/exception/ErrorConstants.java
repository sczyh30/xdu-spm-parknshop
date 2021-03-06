package io.spm.parknshop.common.exception;

/**
 * Error constants class.
 *
 * @author Eric Zhao
 */
public final class ErrorConstants {

  /**
   * From original HTTP response status code.
   */
  public static final int BAD_REQUEST = 400;
  public static final int NO_AUTH = 401;
  public static final int FORBIDDEN = 403;
  public static final int NOT_FOUND = 404;
  public static final int SERVER_ERROR = 500;

  // Not exist: 404x
  public static final int NOT_EXIST = 4040;
  public static final int USER_NOT_EXIST = 4041;
  public static final int USER_DELIVERY_ADDRESS_NOT_EXIST = 40413;
  public static final int PRODUCT_NOT_EXIST = 40421;
  public static final int PRODUCT_DELETED = 40423;
  public static final int PRODUCT_NOT_EXIST_IN_CART = 40422;
  public static final int COMMENT_NOT_EXIST = 40429;
  public static final int STORE_NOT_EXIST = 4043;
  public static final int APPLY_NOT_EXIST = 4045;
  public static final int ORDER_NOT_EXIST = 4046;
  public static final int SUB_ORDER_NOT_EXIST = 40462;
  public static final int PAYMENT_NOT_EXIST = 4047;
  public static final int AD_NOT_EXIST = 4048;

  // Product exception: 460x
  public static final int PRODUCT_REMOVED = 4600;
  public static final int PRODUCT_UNAVAILABLE = 4601;

  // Cart exception: 461x
  public static final int EMPTY_CART = 4611;

  // Payment exception: 4620 - 4649
  public static final int PAYMENT_CANCELED_OR_FINISHED = 4620;
  public static final int PAYMENT_ALREADY_STARTED = 4621;
  public static final int UNKNOWN_PAYMENT_TYPE = 4622;
  public static final int PAYMENT_CANNOT_CALL_PAY_INTERFACE = 4623;
  public static final int PAYMENT_CANCEL_PAY_FAIL = 4624;
  public static final int PAYMENT_REFUND_FAIL = 4625;
  public static final int PAYMENT_TRANSFER_TRANSACTION_FAIL = 4626;

  // Refund exception: 4650 - 4659
  public static final int REFUND_ALREADY_STARTED = 4650;
  public static final int REFUND_ALREADY_COMPLETED = 4651;
  public static final int REFUND_INVALID_OPERATION = 4652;
  public static final int REFUND_NOT_EXIST = 4654;

  // Order exception: 47xx
  public static final int ORDER_UNEXPECTED_DATA = 4701;
  public static final int ORDER_UNEXPECTED_STATE = 4702;

  // Apply workflow exception: 4900 - 4939
  public static final int ILLEGAL_APPLY_TYPE = 4901;

  // Comment exception: 4940 - 4944
  public static final int DUPLICATE_COMMENT = 4940;
  public static final int COMMENT_NOT_ALLOW_NOT_BUY = 4941;

  public static final int USER_ALREADY_EXISTS = 4110;

  public static final int USER_MODIFY_OLD_PASSWORD_NOT_MATCH = 4115;
  public static final int USER_INFO_DUPLICATE = 4111;

  public static final int ID_NOT_MATCH = 4001;

  public static final int USER_LOGIN_INCORRECT = 4011;
  public static final int USER_INVALID_TOKEN = 4012;
  public static final int USER_ROLE_NO_PERMISSION = 4013;

  public static final int INTERNAL_UNKNOWN_ERROR = 4444;

  public static final int STORE_APPLY_IN_PROGRESS = 7311;
  public static final int STORE_ALREADY_OPEN = 7312;
  public static final int STORE_APPLY_NOT_EXIST = 7313;

  public static final int INVALID_COMMISSION = 8001;
  public static final int INVALID_PRICE = 8002;

  public static final int PRODUCT_NO_INVENTORY = 4610;

  // Advertisement exception: 8100 - 8119
  public static final int AD_UNKNOWN_TYPE = 8100;

  private ErrorConstants() {}
}
