package io.spm.parknshop.trade.domain;

/**
 * @author Eric Zhao
 */
public class PaymentResult {

  private int code;
  private String message;

  private Object extra;

  public PaymentResult() {}

  public PaymentResult(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  public PaymentResult setCode(int code) {
    this.code = code;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public PaymentResult setMessage(String message) {
    this.message = message;
    return this;
  }

  public Object getExtra() {
    return extra;
  }

  public PaymentResult setExtra(Object extra) {
    this.extra = extra;
    return this;
  }
}
