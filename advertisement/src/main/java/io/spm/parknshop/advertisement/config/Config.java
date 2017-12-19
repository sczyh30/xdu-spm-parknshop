package io.spm.parknshop.advertisement.config;

import io.spm.parknshop.apply.event.StateMachine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.spm.parknshop.advertisement.domain.AdApplyEventType.*;
import static io.spm.parknshop.advertisement.domain.AdStatus.*;

@Configuration
public class Config {

  @Bean
  public StateMachine adStateMachine() {
    return StateMachine.builder()
      .addTransformer(NEW_APPLY, SUBMIT_APPLY, NEW_APPLY)
      .addTransformer(NEW_APPLY, REJECT_APPLY, REJECTED)
      .addTransformer(NEW_APPLY, WITHDRAW_APPLY, CANCELED)
      .addTransformer(NEW_APPLY, APPROVE_APPLY, APPROVED)
      .addTransformer(APPROVED, WITHDRAW_APPLY, CANCELED)
      .addTransformer(APPROVED, FINISH_PAY, PAYED)
      .withDefaultState(REJECTED)
      .build();
  }
}
