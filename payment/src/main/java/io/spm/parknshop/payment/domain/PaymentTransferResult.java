package io.spm.parknshop.payment.domain;

public class PaymentTransferResult {

  private String transferTransactionId;

  public String getTransferTransactionId() {
    return transferTransactionId;
  }

  public PaymentTransferResult setTransferTransactionId(String transferTransactionId) {
    this.transferTransactionId = transferTransactionId;
    return this;
  }

  @Override
  public String toString() {
    return "PaymentTransferResult{" +
      "transferTransactionId='" + transferTransactionId + '\'' +
      '}';
  }
}
