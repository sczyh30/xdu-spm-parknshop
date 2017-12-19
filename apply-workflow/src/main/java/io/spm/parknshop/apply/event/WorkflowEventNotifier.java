package io.spm.parknshop.apply.event;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 * @param <R>
 */
@FunctionalInterface
public interface WorkflowEventNotifier<R> {

  Mono<R> doNotify(ApplyEvent event, Apply apply);

}
