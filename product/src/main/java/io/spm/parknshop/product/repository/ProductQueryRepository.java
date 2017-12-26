package io.spm.parknshop.product.repository;

import io.spm.parknshop.category.domain.Category;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.domain.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductQueryRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public List<ProductVO> getByStoreId(long storeId) {
    return jdbcTemplate.query("SELECT a.*, b.name AS catalog_name, c.name AS store_name, d.amount AS inventory " +
      "FROM product a, catalog b, store c, inventory d " +
      "WHERE a.store_id = ? AND a.status = 0 AND a.catalog_id = b.id AND a.store_id = c.id AND a.id = d.id", new Object[] { storeId }, rowMapper);
  }

  public List<ProductVO> getByUserFavorite(long userId) {
    return jdbcTemplate.query("SELECT a.*, b.name AS catalog_name, c.name AS store_name, d.amount AS inventory " +
      "FROM product a, catalog b, store c, inventory d " +
      "WHERE a.id IN (SELECT target_id FROM favorite WHERE user_id = ? AND favorite_type = ?) AND a.status = 0 AND a.catalog_id = b.id AND a.store_id = c.id AND a.id = d.id",
      new Object[] { userId, 2 }, rowMapper);
  }

  public Optional<ProductVO> getProductVO(long id) {
    return jdbcTemplate.query("SELECT a.*, b.name AS catalog_name, c.name AS store_name, d.amount AS inventory " +
      "FROM product a, catalog b, store c, inventory d " +
      "WHERE a.id = ? AND a.catalog_id = b.id AND a.store_id = c.id AND a.id = d.id", new Object[] { id }, resultSetExtractor);
  }

  public List<ProductVO> searchProductVOByCatalog(long catalogId) {
    return jdbcTemplate.query("SELECT a.*, b.name AS catalog_name, c.name AS store_name, d.amount AS inventory " +
      "FROM product a, catalog b, store c, inventory d " +
      "WHERE a.catalog_id = ? AND a.status = 0 AND a.catalog_id = b.id AND a.store_id = c.id AND c.status = 0 AND a.id = d.id", new Object[] { catalogId }, rowMapper);
  }

  public List<ProductVO> getNRecentProductVO(int limit) {
    return jdbcTemplate.query("SELECT a.*, b.name AS catalog_name, c.name AS store_name, d.amount AS inventory " +
      "FROM product a, catalog b, store c, inventory d " +
      "WHERE  a.catalog_id = b.id  AND a.status = 0 AND a.store_id = c.id AND a.id = d.id " +
      "ORDER BY a.gmt_modified LIMIT ?", new Object[] { limit }, rowMapper);
  }

  public List<ProductVO> searchProductVOByKeyword(String keyword) {
    return jdbcTemplate.query("SELECT a.*, b.name AS catalog_name, c.name AS store_name, d.amount AS inventory " +
      "FROM product a, catalog b, store c, inventory d " +
      "WHERE a.name LIKE CONCAT('%',?,'%') AND a.status = 0 AND a.catalog_id = b.id AND a.store_id = c.id AND c.status = 0 AND a.id = d.id", new Object[] { keyword }, rowMapper);
  }

  private RowMapper<ProductVO> rowMapper = (rs, i) -> {
    Product product = new Product().setId(rs.getLong("id"))
      .setCatalogId(rs.getLong("catalog_id"))
      .setStoreId(rs.getLong("store_id"))
      .setGmtModified(rs.getDate("gmt_modified"))
      .setDescription(rs.getString("description"))
      .setName(rs.getString("name"))
      .setPrice(rs.getDouble("price"))
      .setPicUri(rs.getString("pic_uri"));
    Category category = new Category().setId(rs.getLong("catalog_id"))
      .setName(rs.getString("catalog_name"));
    String storeName = rs.getString("store_name");
    int inventory = rs.getInt("inventory");
    return new ProductVO().setProduct(product)
      .setCategory(category).setInventory(inventory).setStoreName(storeName);
  };

  private ResultSetExtractor<Optional<ProductVO>> resultSetExtractor = rs -> {
    if (rs.next()) {
      Product product = new Product().setId(rs.getLong("id"))
        .setCatalogId(rs.getLong("catalog_id"))
        .setStoreId(rs.getLong("store_id"))
        .setGmtModified(rs.getDate("gmt_modified"))
        .setDescription(rs.getString("description"))
        .setName(rs.getString("name"))
        .setPrice(rs.getDouble("price"))
        .setPicUri(rs.getString("pic_uri"));
      Category category = new Category().setId(rs.getLong("catalog_id"))
        .setName(rs.getString("catalog_name"));
      String storeName = rs.getString("store_name");
      int inventory = rs.getInt("inventory");
      return Optional.of(new ProductVO().setProduct(product)
        .setCategory(category).setInventory(inventory).setStoreName(storeName));
    } else {
      return Optional.empty();
    }
  };

}
