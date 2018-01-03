package io.spm.parknshop.apply.service.impl;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.domain.ApplyProcessorRoles;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.event.WorkflowEventAggregator;
import io.spm.parknshop.apply.repository.ApplyEventRepository;
import io.spm.parknshop.apply.repository.ApplyMetadataRepository;
import io.spm.parknshop.apply.service.ApplyDataService;
import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.common.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.spm.parknshop.common.exception.ErrorConstants.*;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

/**
 * @author Eric Zhao
 */
@Service
public class ApplyDataServiceImpl implements ApplyDataService {

  @Autowired
  private ApplyMetadataRepository applyMetadataRepository;
  @Autowired
  private ApplyEventRepository applyEventRepository;

  @Override
  public Flux<ApplyEvent> getEventStream(Long applyId) {
    return checkApplyId(applyId)
      .flatMapMany(v -> asyncIterable(() -> applyEventRepository.getByApplyIdOrderById(applyId)))
      .switchIfEmpty(Mono.error(new ServiceException(APPLY_NOT_EXIST, "Not exist: apply " + applyId)));
  }

  @Override
  public Flux<Apply> getApplyByType(int applyType) {
    return asyncIterable(() -> applyMetadataRepository.getByApplyType(applyType));
  }

  @Override
  public Mono<Boolean> checkApplyExistsFor(String proposerId, int applyType, Set<Integer> statusSet) {
    if (Objects.isNull(statusSet) || statusSet.isEmpty()) {
      return Mono.just(false);
    }
    String range = statusSet.stream().map(Object::toString).collect(Collectors.joining(","));
    return checkProposerId(proposerId)
      .flatMap(v -> async(() -> applyMetadataRepository.getConditionalWithStatusRange(proposerId, applyType, range)))
      .map(List::isEmpty);
  }

  @Override
  public Mono<Apply> getApplyById(Long applyId) {
    return checkApplyId(applyId)
      .flatMap(v -> async(() -> applyMetadataRepository.getById(applyId)))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(APPLY_NOT_EXIST, "Not exist: apply " + applyId)));
  }

  @Override
  public <S, E> Mono<S> transformToNextState(Long applyId, E event, /*@NonNull*/ WorkflowEventAggregator<S, E> aggregator) {
    return aggregator.aggregate(getEventStream(applyId))
      .map(curState -> aggregator.transform(curState, event));
  }

  @Override
  public Mono<Long> performApplyTransform(Long applyId, int event, String processorId, ApplyResult applyResult, /*@NonNull*/ WorkflowEventAggregator<Integer, Integer> aggregator) {
    if (Objects.isNull(applyId) || Objects.isNull(processorId)) {
      return Mono.error(ExceptionUtils.invalidParam("apply"));
    }
    ApplyEvent applyEvent = new ApplyEvent().setApplyId(applyId)
      .setApplyEventType(event)
      .setProcessorId(processorId)
      .setExtraInfo(JsonUtils.toJson(applyResult));
    return transformToNextState(applyId, event, aggregator)
      .flatMap(nextState -> asyncExecute(() -> saveAndUpdate(applyId, applyEvent, nextState)));
  }

  @Transactional
  protected void saveAndUpdate(long applyId, ApplyEvent applyEvent, int nextStatus) {
    applyEventRepository.save(applyEvent);
    applyMetadataRepository.updateStatus(applyId, nextStatus);
  }

  @Override
  public Mono<Tuple2<Apply, ApplyEvent>> saveNewApply(/*@NonNull*/ Apply applyMetadata, /*@NonNull*/ ApplyEvent applyEvent) {
    return async(() -> saveNewApplyInternal(applyMetadata, applyEvent));
  }

  @Transactional
  protected Tuple2<Apply, ApplyEvent> saveNewApplyInternal(/*@NonNull*/ Apply applyMetadata, /*@NonNull*/ ApplyEvent applyEvent) {
    Apply apply = applyMetadataRepository.save(applyMetadata);
    applyEvent.setApplyId(apply.getId());
    ApplyEvent newEvent = applyEventRepository.save(applyEvent);
    return Tuple2.of(apply, newEvent);
  }

  @Override
  public Mono<?> checkAllowPerformCancel(Long applyId, String processorId) {
    if (Objects.isNull(processorId)) {
      return Mono.error(ExceptionUtils.invalidParam("processorId"));
    }
    if (ApplyProcessorRoles.isAdmin(processorId)) {
      return Mono.just(true);
    }
    return getApplyById(applyId)
      .filter(apply -> apply.getProposerId().equals(processorId))
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.USER_ROLE_NO_PERMISSION, "You don't have permission for the operation")));
  }

  @Override
  public Mono<?> checkAllowViewApply(Long applyId, String processorId) {
    return checkAllowPerformCancel(applyId, processorId);
  }

  private Mono<String> checkProposerId(String proposerId) {
    if (Objects.isNull(proposerId)) {
      return Mono.error(ExceptionUtils.invalidParam("proposerId"));
    }
    return Mono.just(proposerId);
  }

  private Mono<Long> checkApplyId(Long applyId) {
    if (Objects.isNull(applyId) || applyId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("applyId"));
    }
    return Mono.just(applyId);
  }
}
