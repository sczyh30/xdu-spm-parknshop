package io.spm.parknshop.store.event;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.event.WorkflowEventNotifier;
import io.spm.parknshop.notify.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@Component
public class StoreApplyEventNotifier implements WorkflowEventNotifier<String> {

  @Autowired
  private NotifyService notifyService;

  @Override
  public Mono<String> doNotify(ApplyEvent event, Apply apply) {
    return null;
  }
}
