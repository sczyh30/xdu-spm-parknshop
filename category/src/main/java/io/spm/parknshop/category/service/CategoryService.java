package io.spm.parknshop.category.service;

import io.spm.parknshop.category.domain.Category;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Interface of category service.
 *
 * @author Eric Zhao
 */
public interface CategoryService {

  Mono<Optional<Category>> getById(Long id);

  Flux<Category> getAll();

  Mono<Category> add(Category category);

  Mono<Category> modify(Long id, Category category);

  Mono<Long> remove(Long id);
}
