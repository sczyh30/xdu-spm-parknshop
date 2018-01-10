package io.spm.parknshop.store.config;

import io.spm.parknshop.common.state.StateMachine;
import io.spm.parknshop.common.state.StateMismatchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.spm.parknshop.apply.domain.ApplyStatus.*;
import static io.spm.parknshop.store.domain.StoreApplyEventType.*;

/**
 * @author Eric Zhao
 */
@Configuration("storeConfig")
public class Config {

  @Bean
  public StateMachine storeApplyStateMachine() {
    return StateMachine.builder()
      .addTransformer(NEW_APPLY, SUBMIT_APPLY, NEW_APPLY)
      .addTransformer(NEW_APPLY, REJECT_APPLY, REJECTED)
      .addTransformer(NEW_APPLY, WITHDRAW_APPLY, CANCELED)
      .addTransformer(NEW_APPLY, APPROVE_APPLY, APPROVED)
      .withStateMismatchStrategy(StateMismatchStrategy.THROW_EXCEPTION)
      .build();
  }

}
