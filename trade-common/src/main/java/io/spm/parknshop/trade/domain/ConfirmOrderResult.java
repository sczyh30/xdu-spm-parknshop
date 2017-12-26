package io.spm.parknshop.trade.domain;

/**
 * @author Eric Zhao
 */
public class ConfirmOrderResult {

  private PaymentRedirectData paymentData;

  public PaymentRedirectData getPaymentData() {
    return paymentData;
  }

  public ConfirmOrderResult setPaymentData(PaymentRedirectData paymentData) {
    this.paymentData = paymentData;
    return this;
  }
}
