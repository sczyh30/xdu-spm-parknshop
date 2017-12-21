package io.spm.parknshop.category.service;

import io.spm.parknshop.category.domain.Category;
import io.spm.parknshop.category.repository.CatalogRepository;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class CategoryServiceImpl implements CategoryService {

  @Autowired
  private CatalogRepository catalogRepository;

  @Override
  public Mono<Optional<Category>> getById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> catalogRepository.findById(id));
  }

  @Override
  public Flux<Category> getAll() {
    return asyncIterable(() -> catalogRepository.findAll());
  }

  @Override
  public Mono<Category> add(Category category) {
    if (Objects.isNull(category) || Objects.isNull(category.getName())) {
      return Mono.error(ExceptionUtils.invalidParam("category"));
    }
    if (Objects.nonNull(category.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("category ID should not be provided"));
    }
    return async(() -> catalogRepository.save(category));
  }

  @Override
  public Mono<Category> modify(Long id, Category category) {
    if (Objects.isNull(category) || Objects.isNull(category.getName()) || Objects.isNull(category.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("category"));
    }
    if (!id.equals(category.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> catalogRepository.save(category));
  }

  @Override
  public Mono<Long> remove(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> catalogRepository.deleteById(id));
  }
}
