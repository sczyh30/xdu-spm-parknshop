package io.spm.parknshop.store.repository;

import io.spm.parknshop.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Data repository for stores.
 */
public interface StoreRepository extends JpaRepository<Store, Long> {

  Optional<Store>  getBySellerId(Long sellerId);

  @Query(value = "SELECT * FROM store where store.name LIKE  CONCAT('%',:keyword,'%')", nativeQuery = true)
  List<Store> searchStoreByKeyword(@Param("keyword")String keyword);

}
