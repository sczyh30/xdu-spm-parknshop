package io.spm.parknshop.product.repository;

import io.spm.parknshop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Interface of product repository.
 *
 * @author Eric Zhao
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> getByStoreId(long id);

  /*@Query(value = "UPDATE product SET is_deleted = 1 WHERE id = ?1", nativeQuery = true)
  @Modifying
  @Override
  void deleteById(Long id);*/
}
