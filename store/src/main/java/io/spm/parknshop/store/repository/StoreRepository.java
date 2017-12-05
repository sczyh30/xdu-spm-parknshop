package io.spm.parknshop.store.repository;

import io.spm.parknshop.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Data repository for stores.
 */
public interface StoreRepository extends JpaRepository<Store, Long> {

}
