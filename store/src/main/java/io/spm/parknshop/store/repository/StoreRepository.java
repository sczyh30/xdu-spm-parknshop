package io.spm.parknshop.store.repository;

import io.spm.parknshop.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Data repository for stores.
 */
public interface StoreRepository extends JpaRepository<Store, Long> {

  Optional<Store>  getBySellerId(Long sellerId);

}
