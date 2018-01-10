package io.spm.parknshop.advertisement.event;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.event.WorkflowEventNotifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.advertisement.domain.apply.AdApplyEventType.*;

@Component
public class AdApplyEventNotifier implements WorkflowEventNotifier<String> {

  @Override
  public Mono<String> doNotify(ApplyEvent event, Apply apply) {
    return Mono.empty();
  }
}
