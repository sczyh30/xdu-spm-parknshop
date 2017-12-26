package io.spm.parknshop.query.service;

import io.spm.parknshop.query.vo.StoreVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface StoreQueryService {

  Mono<StoreVO> getStoreById(Long id);

  Flux<StoreVO> getAll();
}
