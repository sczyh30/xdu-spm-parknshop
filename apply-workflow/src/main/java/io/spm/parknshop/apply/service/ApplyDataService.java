package io.spm.parknshop.apply.service;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.event.WorkflowEventAggregator;
import io.spm.parknshop.common.functional.Tuple2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * @author Eric Zhao
 */
public interface ApplyDataService {

  Flux<ApplyEvent> getEventStream(Long applyId);

  Mono<Apply> getApplyById(Long applyId);

  Flux<Apply> getApplyByType(int applyType);

  Mono<Boolean> checkApplyExistsFor(String proposerId, int applyType, Set<Integer> statusSet);

  <S, E> Mono<S> transformToNextState(Long applyId, E event, WorkflowEventAggregator<S, E> aggregator);

  Mono<Long> performApplyTransform(Long applyId, int event, String processorId, ApplyResult applyResult, WorkflowEventAggregator<Integer, Integer> aggregator);

  /**
   * Save new apply synchronously, in the same transaction.
   *
   * @param applyMetadata apply domain, should be checked
   * @param applyEvent apply submit event, should be checked (NEW_SUBMIT type)
   * @return the apply and apply event entity
   */
  Mono<Tuple2<Apply, ApplyEvent>> saveNewApply(Apply applyMetadata, ApplyEvent applyEvent);

  Mono<?> checkAllowPerformCancel(Long applyId, String processorId);

  Mono<?> checkAllowViewApply(Long applyId, String processorId);

}
