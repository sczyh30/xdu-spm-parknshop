package io.spm.parknshop.apply.domain;

public class RejectResult {

  private int code;
  private String message;

  public int getCode() {
    return code;
  }

  public RejectResult setCode(int code) {
    this.code = code;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public RejectResult setMessage(String message) {
    this.message = message;
    return this;
  }
}
