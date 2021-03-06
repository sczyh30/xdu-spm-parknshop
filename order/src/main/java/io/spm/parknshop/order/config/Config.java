package io.spm.parknshop.order.config;

import io.spm.parknshop.common.state.StateMachine;
import io.spm.parknshop.order.domain.OrderEventType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.spm.parknshop.order.domain.OrderStatus.*;

@Configuration("orderConfig")
public class Config {

  @Bean
  public StateMachine orderStateMachine() {
    return StateMachine.builder()
      .addTransformer(NEW_CREATED, OrderEventType.NEW_CREATED, NEW_CREATED)
      .addTransformer(NEW_CREATED, OrderEventType.CANCEL_ORDER, CANCELED)
      .addTransformer(NEW_CREATED, OrderEventType.FINISH_PAY, PAYED)
      .addTransformer(PAYED, OrderEventType.PROCESS_ORDER_SHIPMENT, PREPARING_SHIPMENT)
      .addTransformer(PAYED, OrderEventType.REFUND_COMPLETE, REFUNDED)
      .addTransformer(PREPARING_SHIPMENT, OrderEventType.FINISH_SHIPMENT, SHIPPED)
      .addTransformer(PREPARING_SHIPMENT, OrderEventType.REFUND_COMPLETE, REFUNDED)
      .addTransformer(SHIPPED, OrderEventType.FINISH_DELIVERY, DELIVERED)
      .addTransformer(SHIPPED, OrderEventType.CONFIRM_ORDER, COMPLETED)
      .addTransformer(SHIPPED, OrderEventType.REFUND_COMPLETE, REFUNDED)
      .addTransformer(DELIVERED, OrderEventType.CONFIRM_ORDER, COMPLETED)
      .addTransformer(DELIVERED, OrderEventType.REFUND_COMPLETE, REFUNDED)
      .addTransformer(COMPLETED, OrderEventType.FINISH_DELIVERY, COMPLETED)
      .withDefaultState(UNEXPECTED_STATE)
      .build();
  }

}
