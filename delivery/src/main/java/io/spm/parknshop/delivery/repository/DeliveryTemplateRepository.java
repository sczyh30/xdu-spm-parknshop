package io.spm.parknshop.delivery.repository;

import io.spm.parknshop.delivery.domain.DeliveryTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryTemplateRepository extends JpaRepository<DeliveryTemplate, Long> {

  List<DeliveryTemplate> getByStoreId(long storeId);

}
