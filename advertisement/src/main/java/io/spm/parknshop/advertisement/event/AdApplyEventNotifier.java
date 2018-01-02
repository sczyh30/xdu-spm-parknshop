package io.spm.parknshop.advertisement.event;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.event.WorkflowEventNotifier;
import io.spm.parknshop.notify.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.advertisement.domain.apply.AdApplyEventType.*;

@Component
public class AdApplyEventNotifier implements WorkflowEventNotifier<String> {

  @Autowired
  private NotifyService notifyService;

  @Override
  public Mono<String> doNotify(ApplyEvent event, Apply apply) {
    switch (event.getApplyEventType()) {
      case SUBMIT_APPLY:
        return doNotifyNewApply(apply);
      case APPROVE_APPLY:

      case REJECT_APPLY:
      case WITHDRAW_APPLY:
      default:
        return Mono.empty();
    }
  }

  private Mono<String> doNotifyNewApply(Apply apply) {


    return Mono.just("1");
  }
}
