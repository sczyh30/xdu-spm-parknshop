package io.spm.parknshop.advertisement.event;

import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.domain.ApplyStatus;
import io.spm.parknshop.apply.event.StateMachine;
import io.spm.parknshop.apply.event.WorkflowEventAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.advertisement.domain.AdStatus.*;

/**
 * @author Eric Zhao
 */
@Component
public class AdApplyEventAggregator implements WorkflowEventAggregator<Integer> {

  @Autowired
  @Qualifier(value = "adStateMachine")
  private StateMachine stateMachine;

  @Override
  public Mono<Integer> aggregate(Flux<ApplyEvent> eventStream) {
    return eventStream.map(ApplyEvent::getApplyEventType)
      .reduce(ApplyStatus.NEW_APPLY, stateMachine::transform);
  }
}
