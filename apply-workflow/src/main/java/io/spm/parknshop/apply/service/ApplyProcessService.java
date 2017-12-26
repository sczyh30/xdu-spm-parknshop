package io.spm.parknshop.apply.service;

import io.spm.parknshop.apply.domain.ApplyResult;
import reactor.core.publisher.Mono;

public interface ApplyProcessService {

  Mono<Long> rejectApply(Long applyId, String processorId, ApplyResult applyResult);

  Mono<Long> approveApply(Long applyId, String processorId, ApplyResult applyResult);

  Mono<Long> cancelApply(Long applyId, String processorId);
}
