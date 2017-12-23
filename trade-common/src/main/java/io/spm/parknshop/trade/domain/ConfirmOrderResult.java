package io.spm.parknshop.trade.domain;

/**
 * @author Eric Zhao
 */
public class ConfirmOrderResult {
  private Long paymentId;
  private String redirectUrl;
  private String renderForm;

  public Long getPaymentId() {
    return paymentId;
  }

  public ConfirmOrderResult setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
    return this;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }

  public ConfirmOrderResult setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
    return this;
  }

  public String getRenderForm() {
    return renderForm;
  }

  public ConfirmOrderResult setRenderForm(String renderForm) {
    this.renderForm = renderForm;
    return this;
  }
}
