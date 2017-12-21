package io.spm.parknshop.delivery.service.impl;

import io.spm.parknshop.delivery.domain.DeliveryTemplate;
import io.spm.parknshop.delivery.service.DeliveryTemplateService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class DeliveryTemplateServiceImpl implements DeliveryTemplateService {

  @Override
  public Flux<DeliveryTemplate> getTemplateByStoreId(Long storeId) {
    return null;
  }

  @Override
  public Mono<Optional<DeliveryTemplate>> getById(Long id) {
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
