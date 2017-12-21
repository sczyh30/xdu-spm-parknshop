package io.spm.parknshop.apply.service;

import io.spm.parknshop.apply.domain.ApplyResult;
import reactor.core.publisher.Mono;

public interface ApplyOperationService {

  Mono<Long> reject(Long applyId, String processorId, ApplyResult applyResult);

  Mono<Long> approve(Long applyId, String processorId, ApplyResult applyResult);

  Mono<Long> cancel(Long applyId, String processorId);
}
