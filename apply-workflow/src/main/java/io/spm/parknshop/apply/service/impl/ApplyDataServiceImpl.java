package io.spm.parknshop.apply.service.impl;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.repository.ApplyEventRepository;
import io.spm.parknshop.apply.repository.ApplyMetadataRepository;
import io.spm.parknshop.apply.service.ApplyDataService;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.exception.ErrorConstants.*;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class ApplyDataServiceImpl implements ApplyDataService {

  @Autowired
  private ApplyMetadataRepository applyMetadataRepository;
  @Autowired
  private ApplyEventRepository applyEventRepository;

  @Override
  public Flux<ApplyEvent> getEventStream(Long applyId) {
    return checkApplyId(applyId)
      .flatMapMany(v -> asyncIterable(() -> applyEventRepository.getByApplyId(applyId)));
  }

  @Override
  public Mono<Apply> getApplyById(Long applyId) {
    return checkApplyId(applyId)
      .flatMap(v -> async(() -> applyMetadataRepository.getById(applyId)))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(APPLY_NOT_EXIST, "Not exist: apply " + applyId)));
  }

  private Mono<Long> checkApplyId(Long applyId) {
    if (Objects.isNull(applyId) || applyId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("applyId"));
    }
    return Mono.just(applyId);
  }
}
