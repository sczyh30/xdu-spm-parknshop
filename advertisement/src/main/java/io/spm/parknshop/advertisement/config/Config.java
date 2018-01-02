package io.spm.parknshop.advertisement.config;

import io.spm.parknshop.advertisement.domain.AdStatus;
import io.spm.parknshop.common.state.StateMachine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.spm.parknshop.apply.domain.ApplyStatus.*;
import static io.spm.parknshop.advertisement.domain.apply.AdApplyEventType.*;

@Configuration("adConfig")
public class Config {

  @Bean
  public StateMachine adStateMachine() {
    return StateMachine.builder()
      .addTransformer(NEW_APPLY, SUBMIT_APPLY, NEW_APPLY)
      .addTransformer(NEW_APPLY, REJECT_APPLY, REJECTED)
      .addTransformer(NEW_APPLY, WITHDRAW_APPLY, CANCELED)
      .addTransformer(NEW_APPLY, APPROVE_APPLY, APPROVED)
      .addTransformer(APPROVED, WITHDRAW_APPLY, CANCELED)
      .addTransformer(APPROVED, FINISH_PAY, AdStatus.PAYED)
      .withDefaultState(REJECTED)
      .build();
  }
}
