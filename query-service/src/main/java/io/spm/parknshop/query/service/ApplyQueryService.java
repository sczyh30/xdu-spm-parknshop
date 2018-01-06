package io.spm.parknshop.query.service;

import io.spm.parknshop.apply.domain.ApplyVO;
import io.spm.parknshop.query.vo.apply.ApplyListSimpleVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface ApplyQueryService {

  Mono<ApplyVO> renderApplyView(Long applyId, String currentRole);

  Flux<ApplyListSimpleVO> getAll();
}
