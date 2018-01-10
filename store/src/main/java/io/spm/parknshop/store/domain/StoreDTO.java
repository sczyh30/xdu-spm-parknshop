package io.spm.parknshop.store.domain;

/**
 * @author Eric Zhao
 */
public class StoreDTO {

  private Long sellerId;
  private String name;

  private String briefDescription;
  private String telephone;
  private String email;

  public Long getSellerId() {
    return sellerId;
  }

  public StoreDTO setSellerId(Long sellerId) {
    this.sellerId = sellerId;
    return this;
  }

  public String getName() {
    return name;
  }

  public StoreDTO setName(String name) {
    this.name = name;
    return this;
  }

  public String getBriefDescription() {
    return briefDescription;
  }

  public StoreDTO setBriefDescription(String briefDescription) {
    this.briefDescription = briefDescription;
    return this;
  }

  public String getTelephone() {
    return telephone;
  }

  public StoreDTO setTelephone(String telephone) {
    this.telephone = telephone;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public StoreDTO setEmail(String email) {
    this.email = email;
    return this;
  }
}
