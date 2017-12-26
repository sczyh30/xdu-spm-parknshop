package io.spm.parknshop.apply.service;

import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface ApplyDataService {

  Flux<ApplyEvent> getEventStream(Long applyId);

  Mono<Apply> getApplyById(Long applyId);

}
