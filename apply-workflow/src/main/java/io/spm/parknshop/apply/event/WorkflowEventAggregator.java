package io.spm.parknshop.apply.event;

import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.common.state.Transformer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Event aggregator for apply workflow.
 *
 * @author Eric Zhao
 *
 * @param <S> final status of an target
 */
public interface WorkflowEventAggregator<S, E> extends Transformer<S, E> {

  Mono<S> aggregate(Flux<ApplyEvent> eventStream);

}
