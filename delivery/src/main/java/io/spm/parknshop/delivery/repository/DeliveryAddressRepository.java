package io.spm.parknshop.delivery.repository;

import io.spm.parknshop.delivery.domain.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

  @Query(value = "SELECT * FROM delivery_address WHERE user_id = ?1 AND status = 0", nativeQuery = true)
  List<DeliveryAddress> getByUserId(long userId);

  @Override
  @Query(value = "SELECT * FROM delivery_address WHERE id = ?1 AND status = 0", nativeQuery = true)
  Optional<DeliveryAddress> findById(Long id);

  @Query(value = "SELECT * FROM delivery_address WHERE id = ?1", nativeQuery = true)
  Optional<DeliveryAddress> findByIdWithDeleted(Long id);

  @Override
  @Transactional
  @Modifying
  @Query(value = "UPDATE delivery_address SET status = 1, gmt_modified = CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
  void deleteById(Long id);
}
