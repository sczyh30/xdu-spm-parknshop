package io.spm.parknshop.catalog.repository;

import io.spm.parknshop.catalog.domain.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

}
