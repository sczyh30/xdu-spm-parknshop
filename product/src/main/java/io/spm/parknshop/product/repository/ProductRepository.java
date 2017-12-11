package io.spm.parknshop.product.repository;

import io.spm.parknshop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Interface of product repository.
 *
 * @author Eric Zhao
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> getByStoreId(long storeId);

  List<Product> getByCatalogId(long catalogId);

  @Query(value = "SELECT * FROM product WHERE product.name LIKE CONCAT('%',:keyword,'%')", nativeQuery = true)
  List<Product> searchByKeyword(@Param("keyword") String keyword);
}
