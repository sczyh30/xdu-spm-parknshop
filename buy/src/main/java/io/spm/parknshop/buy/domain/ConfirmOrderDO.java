package io.spm.parknshop.buy.domain;

/**
 * @author Eric Zhao
 */
public class ConfirmOrderDO {

  private Long userId;
  private Long addressId;

  public Long getUserId() {
    return userId;
  }

  public ConfirmOrderDO setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Long getAddressId() {
    return addressId;
  }

  public ConfirmOrderDO setAddressId(Long addressId) {
    this.addressId = addressId;
    return this;
  }
}
