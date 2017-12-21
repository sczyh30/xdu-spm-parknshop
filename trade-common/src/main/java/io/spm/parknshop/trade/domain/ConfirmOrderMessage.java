package io.spm.parknshop.trade.domain;

import io.spm.parknshop.delivery.domain.DeliveryAddress;

public class ConfirmOrderMessage {

  private OrderPreview orderPreview;
  private DeliveryAddress deliveryAddress;

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
