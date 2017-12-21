package io.spm.parknshop.delivery.service;

import io.spm.parknshop.delivery.domain.DeliveryTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface DeliveryTemplateService {

  /**
   * Get delivery templates by store. The implementation must provide a default template
   * if the template for the store is absent.
   *
   * @param storeId store ID
   * @return delivery templates
   */
  Flux<DeliveryTemplate> getTemplateByStoreId(Long storeId);

  Mono<Optional<DeliveryTemplate>> getById(Long id);

  Mono<DeliveryTemplate> addTemplate(DeliveryTemplate template);

  Mono<DeliveryTemplate> updateTemplate(Long id, DeliveryTemplate template);

  Mono<Long> deleteTemplate(Long id);
}
