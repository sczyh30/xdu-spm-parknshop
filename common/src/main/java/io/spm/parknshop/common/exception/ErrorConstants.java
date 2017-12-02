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

  private ErrorConstants() {}
}