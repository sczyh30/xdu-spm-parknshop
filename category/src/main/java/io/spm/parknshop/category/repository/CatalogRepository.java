package io.spm.parknshop.category.repository;

import io.spm.parknshop.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogRepository extends JpaRepository<Category, Long> {

}
