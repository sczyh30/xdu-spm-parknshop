package io.spm.parknshop.catalog.service;

import io.spm.parknshop.catalog.domain.Catalog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface CatalogService {

  Mono<Optional<Catalog>> getById(Long id);

  Flux<Catalog> getAll();

  Mono<Catalog> add(Catalog catalog);

  Mono<Catalog> modify(Long id, Catalog catalog);

  Mono<Long> remove(Long id);
}
