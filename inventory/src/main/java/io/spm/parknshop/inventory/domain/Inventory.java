package io.spm.parknshop.inventory.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Inventory {

  @Id
  private Long id;
  private Date gmtModified;

  private Integer amount;

  public Long getId() {
    return id;
  }

  public Inventory setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public Inventory setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Integer getAmount() {
    return amount;
  }

  public Inventory setAmount(Integer amount) {
    this.amount = amount;
    return this;
  }
}
