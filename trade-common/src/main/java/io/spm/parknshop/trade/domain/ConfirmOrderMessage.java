package io.spm.parknshop.trade.domain;

import io.spm.parknshop.delivery.domain.DeliveryAddress;

public class ConfirmOrderMessage {

  private Long creatorId;
  private OrderPreview orderPreview;
  private DeliveryAddress deliveryAddress;

  public Long getCreatorId() {
    return creatorId;
  }

  public ConfirmOrderMessage setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
    return this;
  }

  public OrderPreview getOrderPreview() {
    return orderPreview;
  }

  public ConfirmOrderMessage setOrderPreview(OrderPreview orderPreview) {
    this.orderPreview = orderPreview;
    return this;
  }

  public DeliveryAddress getDeliveryAddress() {
    return deliveryAddress;
  }

  public ConfirmOrderMessage setDeliveryAddress(DeliveryAddress deliveryAddress) {
    this.deliveryAddress = deliveryAddress;
    return this;
  }
}
