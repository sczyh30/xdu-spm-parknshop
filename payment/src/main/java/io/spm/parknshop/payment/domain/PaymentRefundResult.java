package io.spm.parknshop.payment.domain;

public class PaymentRefundResult {

  private String outerTradeNo;
  private String originPaymentId;
  private String refundNo;

  private boolean changed;

  private double refundFee;

  public PaymentRefundResult() {
  }

  public PaymentRefundResult(String outerTradeNo, String originPaymentId, String refundNo, double refundFee) {
    this.outerTradeNo = outerTradeNo;
    this.originPaymentId = originPaymentId;
    this.refundNo = refundNo;
    this.refundFee = refundFee;
  }

  public String getOuterTradeNo() {
    return outerTradeNo;
  }

  public PaymentRefundResult setOuterTradeNo(String outerTradeNo) {
    this.outerTradeNo = outerTradeNo;
    return this;
  }

  public String getOriginPaymentId() {
    return originPaymentId;
  }

  public PaymentRefundResult setOriginPaymentId(String originPaymentId) {
    this.originPaymentId = originPaymentId;
    return this;
  }

  public String getRefundNo() {
    return refundNo;
  }

  public PaymentRefundResult setRefundNo(String refundNo) {
    this.refundNo = refundNo;
    return this;
  }

  public double getRefundFee() {
    return refundFee;
  }

  public PaymentRefundResult setRefundFee(double refundFee) {
    this.refundFee = refundFee;
    return this;
  }

  public boolean isChanged() {
    return changed;
  }

  public PaymentRefundResult setChanged(boolean changed) {
    this.changed = changed;
    return this;
  }

  @Override
  public String toString() {
    return "PaymentRefundResult{" +
      "outerTradeNo='" + outerTradeNo + '\'' +
      ", originPaymentId='" + originPaymentId + '\'' +
      ", refundNo='" + refundNo + '\'' +
      ", changed=" + changed +
      ", refundFee=" + refundFee +
      '}';
  }
}
