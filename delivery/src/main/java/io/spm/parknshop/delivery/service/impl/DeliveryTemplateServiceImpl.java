package io.spm.parknshop.delivery.service.impl;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.delivery.domain.DeliveryTemplate;
import io.spm.parknshop.delivery.repository.DeliveryTemplateRepository;
import io.spm.parknshop.delivery.service.DeliveryTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

@Service
public class DeliveryTemplateServiceImpl implements DeliveryTemplateService {

  @Autowired
  private DeliveryTemplateRepository deliveryTemplateRepository;

  @Override
  public Flux<DeliveryTemplate> getTemplateByStoreId(Long storeId) {
    if (Objects.isNull(storeId) || storeId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("storeId"));
    }
    return asyncIterable(() -> deliveryTemplateRepository.getByStoreId(storeId));
  }

  @Override
  public Mono<Optional<DeliveryTemplate>> getById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return null;
  }

  @Override
  public Mono<DeliveryTemplate> addTemplate(DeliveryTemplate template) {
    return null;
  }

  @Override
  public Mono<DeliveryTemplate> updateTemplate(Long id, DeliveryTemplate template) {
    return null;
  }

  @Override
  public Mono<Long> deleteTemplate(Long id) {
    return null;
  }
}
