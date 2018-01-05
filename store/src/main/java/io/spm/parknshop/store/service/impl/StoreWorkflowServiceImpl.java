package io.spm.parknshop.store.service.impl;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.domain.ApplyProcessorRoles;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.domain.ApplyStatus;
import io.spm.parknshop.apply.service.ApplyDataService;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.common.util.JsonUtils;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.store.domain.StoreApplyEventType;
import io.spm.parknshop.store.domain.StoreDTO;
import io.spm.parknshop.store.domain.StoreStatus;
import io.spm.parknshop.store.event.StoreApplyEventAggregator;
import io.spm.parknshop.store.event.StoreApplyEventNotifier;
import io.spm.parknshop.store.service.StoreService;
import io.spm.parknshop.store.service.StoreWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * Workflow service implementation for shop apply.
 *
 * @author Eric Zhao
 */
@Service("storeApplyService")
public class StoreWorkflowServiceImpl implements StoreWorkflowService {

  @Autowired
  private ApplyDataService applyDataService;
  @Autowired
  private StoreService storeService;

  @Autowired
  private StoreApplyEventAggregator applyEventAggregator;
  @Autowired
  private StoreApplyEventNotifier applyEventNotifier;

  @Override
  public Mono<Long> applyFor(String proposerId, StoreDTO store) {
    return checkApplyParams(proposerId, store)
      .flatMap(v -> checkAllowNewApply(proposerId))
      .flatMap(sellerId -> submitNewApply(proposerId, store.setSellerId(sellerId))
        .map(e -> e.r1.getId())
      );
  }

  private Mono<Tuple2<Apply, ApplyEvent>> submitNewApply(String proposerId, StoreDTO store) {
    Apply applyMetadata = new Apply().setApplyType(STORE_APPLY)
      .setApplyData(JsonUtils.toJson(store))
      .setProposerId(proposerId)
      .setStatus(ApplyStatus.NEW_APPLY);
    ApplyEvent applyEvent = new ApplyEvent().setApplyEventType(StoreApplyEventType.SUBMIT_APPLY)
      .setProcessorId(proposerId);
    return applyDataService.saveNewApply(applyMetadata, applyEvent);
  }

  private Mono<Long> checkAllowNewApply(String proposerId) {
    return ApplyProcessorRoles.checkSellerId(proposerId)
      .flatMap(sellerId -> checkIfOwnedStore(sellerId)
        .flatMap(v -> checkPendingApply(proposerId))
        .map(e -> sellerId)
      );
  }

  private Mono<?> checkIfOwnedStore(long sellerId) {
    return storeService.getBySellerId(sellerId)
      .flatMap(opt -> {
        if (opt.isPresent()) {
          return Mono.error(new ServiceException(STORE_ALREADY_OPEN, "You already have a store"));
        } else {
          return Mono.just(sellerId);
        }
      });
  }

  private Mono<?> checkPendingApply(String proposerId) {
    Set<Integer> statusSet = Collections.singleton(ApplyStatus.NEW_APPLY);
    return applyDataService.checkApplyExistsFor(proposerId, STORE_APPLY, statusSet)
      .flatMap(pending -> {
        if (pending) {
          return Mono.error(new ServiceException(STORE_APPLY_IN_PROGRESS, "You already have a store apply in progress"));
        } else {
          return Mono.just(pending);
        }
      });
  }

  @Override
  public Mono<Long> rejectApply(Long applyId, String processorId, ApplyResult applyResult) {
    return ApplyProcessorRoles.checkAdminId(processorId)
      .flatMap(adminId -> applyDataService.performApplyTransform(applyId, StoreApplyEventType.REJECT_APPLY, processorId, applyResult, applyEventAggregator));
  }

  @Override
  public Mono<Long> approveApply(Long applyId, String processorId, final ApplyResult applyResult) {
    return ApplyProcessorRoles.checkAdminId(processorId)
      .flatMap(adminId -> applyDataService.performApplyTransform(applyId, StoreApplyEventType.APPROVE_APPLY, processorId, applyResult, applyEventAggregator))
      .flatMap(v -> triggerApplyApproveHook(applyId));
  }

  private Mono<Long> triggerApplyApproveHook(/*@NonNull*/ long applyId) {
    return applyDataService.getApplyById(applyId)
      .map(apply -> wrapStore(JsonUtils.parse(apply.getApplyData(), StoreDTO.class)))
      .flatMap(store -> storeService.addStore(store))
      .map(Store::getId);
  }

  private Store wrapStore(StoreDTO storeDTO) {
    return new Store()
      .setName(storeDTO.getName())
      .setSellerId(storeDTO.getSellerId())
      .setBriefDescription(storeDTO.getBriefDescription())
      .setEmail(storeDTO.getEmail())
      .setTelephone(storeDTO.getTelephone())
      .setStatus(StoreStatus.NORMAL);
  }

  @Override
  public Mono<Long> cancelApply(Long applyId, String processorId, ApplyResult applyResult) {
    return applyDataService.checkAllowPerformCancel(applyId, processorId)
      .flatMap(v -> applyDataService.performApplyTransform(applyId, StoreApplyEventType.WITHDRAW_APPLY, processorId, applyResult, applyEventAggregator));
  }

  private Mono<StoreDTO> checkApplyParams(String proposerId, StoreDTO store) {
    return checkProposerId(proposerId)
      .flatMap(v -> checkStoreApplyRequest(store));
  }

  private Mono<String> checkProposerId(String proposerId) {
    if (Objects.isNull(proposerId)) {
      return Mono.error(ExceptionUtils.invalidParam("proposerId"));
    }
    return Mono.just(proposerId);
  }

  private Mono<StoreDTO> checkStoreApplyRequest(StoreDTO store) {
    return Optional.ofNullable(store)
      .map(e -> store.getName())
      .map(e -> store.getEmail())
      .map(e -> store.getBriefDescription())
      .map(e -> store.getTelephone())
      .map(e -> Mono.just(store))
      .orElse(Mono.error(ExceptionUtils.invalidParam("store apply")));
  }
}
