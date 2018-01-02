package io.spm.parknshop.trade.domain;

/**
 * @author Eric Zhao
 */
public class SubmitOrderResult {

  private PaymentRedirectData paymentData;

  public PaymentRedirectData getPaymentData() {
    return paymentData;
  }

  public SubmitOrderResult setPaymentData(PaymentRedirectData paymentData) {
    this.paymentData = paymentData;
    return this;
  }
}
