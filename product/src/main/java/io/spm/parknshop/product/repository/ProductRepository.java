package io.spm.parknshop.product.repository;

import io.spm.parknshop.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Interface of product repository.
 *
 * @author Eric Zhao
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query(value = "SELECT count(*) FROM product WHERE store_id = ?1 AND status = 0", nativeQuery = true)
  int countByStoreId(long storeId);

  @Query(value = "SELECT * FROM product WHERE store_id = ?1 AND status = 0", nativeQuery = true)
  List<Product> getByStoreId(long storeId);

  @Query(value = "SELECT * FROM product WHERE catalog_id = ?1 AND status = 0", nativeQuery = true)
  List<Product> getByCatalogId(long catalogId);

  @Query(value = "SELECT * FROM product WHERE product.name LIKE CONCAT('%',:keyword,'%') AND status = 0", nativeQuery = true)
  List<Product> searchByKeyword(@Param("keyword") String keyword);

  @Modifying
  @Query(value = "UPDATE product SET pic_uri = ?1, gmt_modified = CURRENT_TIMESTAMP WHERE id = ?2", nativeQuery = true)
  @Transactional
  void modifyProductPicUrl(String url, long id);

  @Modifying
  @Query(value = "UPDATE product SET status = 4, gmt_modified = CURRENT_TIMESTAMP WHERE id = ?1", nativeQuery = true)
  @Transactional
  void markAsDeleted(long id);

  @Modifying
  @Query(value = "UPDATE product SET status = 4, gmt_modified = CURRENT_TIMESTAMP WHERE store_id = ?1", nativeQuery = true)
  @Transactional
  void deleteShopProducts(long shopId);

  @Query(value = "SELECT * FROM product WHERE id = ?1", nativeQuery = true)
  Optional<Product> findByIdWithDeleted(Long id);

  @Override
  @Query(value = "SELECT * FROM product WHERE id = ?1 AND status = 0", nativeQuery = true)
  Optional<Product> findById(Long aLong);
}
