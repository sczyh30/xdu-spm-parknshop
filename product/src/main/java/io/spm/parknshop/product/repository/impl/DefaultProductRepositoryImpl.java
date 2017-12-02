package io.spm.parknshop.product.repository.impl;

import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Default implementation of {@link ProductRepository}.
 *
 * @author Eric Zhao
 */
@Deprecated
public class DefaultProductRepositoryImpl /*implements ProductRepository*/ {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Transactional(readOnly = true)
  public Optional<Product> getById(long id) {
    return Optional.of(jdbcTemplate.query("SELECT * FROM product WHERE id = ? AND is_deleted = 0", new Object[] { id }, mapper))
      .filter(e -> !e.isEmpty())
      .map(e -> e.get(0));
  }

  private static final RowMapper<Product> mapper = (rs, n) -> {
    return new Product().setId(rs.getLong("id"))
      .setGmtCreate(rs.getDate("gmt_create"))
      .setGmtModified(rs.getDate("gmt_modified"))
      .setCatalogId(rs.getLong("catalog_id"))
      .setName(rs.getString("name"))
      .setStoreId(rs.getLong("store_id"))
      .setPrice(rs.getDouble("price"))
      .setDescription(rs.getString("description"))
      .setPicUri(rs.getString("pic_uri"));
  };
}
