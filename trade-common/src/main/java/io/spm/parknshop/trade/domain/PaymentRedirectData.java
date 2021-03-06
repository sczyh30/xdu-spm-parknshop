package io.spm.parknshop.trade.domain;

public class PaymentRedirectData {

  private String paymentId;
  private Integer paymentType;
  private String redirectUrl;
  private String renderForm;

  public String getPaymentId() {
    return paymentId;
  }

  public PaymentRedirectData setPaymentId(String paymentId) {
    this.paymentId = paymentId;
    return this;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public PaymentRedirectData setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
    return this;
  }

  public String getRenderForm() {
    return renderForm;
  }

  public PaymentRedirectData setRenderForm(String renderForm) {
    this.renderForm = renderForm;
    return this;
  }

  public Integer getPaymentType() {
    return paymentType;
  }

  public PaymentRedirectData setPaymentType(Integer paymentType) {
    this.paymentType = paymentType;
    return this;
  }
}
