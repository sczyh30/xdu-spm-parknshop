package io.spm.parknshop.query.service;

import io.spm.parknshop.apply.domain.ApplyVO;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface ApplyQueryService {

  Mono<ApplyVO> renderApplyView(Long applyId, String currentRole);
}
