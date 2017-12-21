package io.spm.parknshop.common.exception;

/**
 * Common exception for biz service. Each kind of error should have a unique error code.
 *
 * @author Eric Zhao
 * @date 2017/12/02
 */
public class ServiceException extends Exception {

  private final int errorCode;
  private Object attach;

  public ServiceException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }


  public ServiceException(int errorCode, String message, Object attach) {
    super(message);
    this.errorCode = errorCode;
    this.attach = attach;
  }

  public ServiceException(int errorCode, Throwable ex) {
    super(ex);
    this.errorCode = errorCode;
  }

  public ServiceException(int errorCode, Throwable ex, Object attach) {
    super(ex);
    this.errorCode = errorCode;
    this.attach = attach;
  }

  public ServiceException(int errorCode, String message, Throwable ex) {
    super(message, ex);
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public Object getAttach() {
    return attach;
  }

  public ServiceException setAttach(Object attach) {
    this.attach = attach;
    return this;
  }
}
