package io.spm.parknshop.delivery.repository;

import io.spm.parknshop.delivery.domain.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Eric Zhao
 */
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

  List<DeliveryAddress> getByUserId(long userId);

}
