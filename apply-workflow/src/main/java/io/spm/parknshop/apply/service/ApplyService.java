package io.spm.parknshop.apply.service;

import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface ApplyService<T, R> {

  Mono<R> applyFor(Long proposerId, T entity);

}
