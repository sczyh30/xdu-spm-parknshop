package io.spm.parknshop.apply.event;

import io.spm.parknshop.apply.domain.ApplyEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Event aggregator for apply workflow.
 *
 * @author Eric Zhao
 *
 * @param <S> final status of an target
 */
@FunctionalInterface
public interface WorkflowEventAggregator<S> {

  Mono<S> aggregate(Flux<ApplyEvent> eventStream);

}
