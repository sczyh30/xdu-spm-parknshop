package io.spm.parknshop.catalog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Eric Zhao
 */
@Entity
public class Catalog {

  @Id
  @GeneratedValue
  private Long id;
  private String name;

  public Long getId() {
    return id;
  }

  public Catalog setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public Catalog setName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public String toString() {
    return "Catalog{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
