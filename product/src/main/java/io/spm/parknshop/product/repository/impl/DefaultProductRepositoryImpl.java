package io.spm.parknshop.product.repository.impl;

import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Default implementation of {@link ProductRepository}.
 *
 * @author Eric Zhao
 */
@Repository
public class DefaultProductRepositoryImpl implements ProductRepository {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public Optional<Product> getById(long id) {
    return Optional.of(jdbcTemplate.query("SELECT * FROM product WHERE id = ?", new Object[] { id }, mapper))
      .filter(e -> !e.isEmpty())
      .map(e -> e.get(0));
  }

  private static final RowMapper<Product> mapper = (rs, n) -> {
    Product product = new Product();
    // TODO: mapper.
    return product;
  };
}
