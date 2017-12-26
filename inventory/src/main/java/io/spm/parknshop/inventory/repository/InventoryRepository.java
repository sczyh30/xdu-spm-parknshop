package io.spm.parknshop.inventory.repository;

import io.spm.parknshop.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  @Query(value = "SELECT amount FROM inventory WHERE id = ?1", nativeQuery = true)
  Optional<Integer> getAmountById(Long id);

  @Modifying
  @Transactional
  @Query(value = "UPDATE inventory SET gmt_modified = CURRENT_TIMESTAMP, amount = ?2 WHERE id = ?1", nativeQuery = true)
  void saveAmount(long id, int amount);
}
