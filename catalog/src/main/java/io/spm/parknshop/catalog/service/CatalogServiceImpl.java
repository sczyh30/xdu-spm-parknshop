package io.spm.parknshop.catalog.service;

import io.spm.parknshop.catalog.domain.Catalog;
import io.spm.parknshop.catalog.repository.CatalogRepository;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class CatalogServiceImpl implements CatalogService {

  @Autowired
  private CatalogRepository catalogRepository;

  @Override
  public Mono<Optional<Catalog>> getById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> catalogRepository.findById(id));
  }

  @Override
  public Flux<Catalog> getAll() {
    return asyncIterable(() -> catalogRepository.findAll());
  }

  @Override
  public Mono<Catalog> add(Catalog catalog) {
    if (Objects.isNull(catalog) || Objects.isNull(catalog.getName())) {
      return Mono.error(ExceptionUtils.invalidParam("catalog"));
    }
    if (Objects.nonNull(catalog.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("catalog ID should not be provided"));
    }
    return async(() -> catalogRepository.save(catalog));
  }

  @Override
  public Mono<Catalog> modify(Long id, Catalog catalog) {
    if (Objects.isNull(catalog) || Objects.isNull(catalog.getName()) || Objects.isNull(catalog.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("catalog"));
    }
    if (!id.equals(catalog.getId())) {
      return Mono.error(ExceptionUtils.idNotMatch());
    }
    return async(() -> catalogRepository.save(catalog));
  }

  @Override
  public Mono<Void> remove(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() -> catalogRepository.deleteById(id));
  }
}
