package io.spm.parknshop.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Admin entity
 *
 * @author four
 */
@Entity
@JsonIgnoreProperties(value = {"password"}, allowSetters = true)
public class Admin {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private String username;
  private String password;

  private String email;

  public Long getId() {
    return id;
  }

  public Admin setId(Long id) {
    this.id = id;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Admin setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public Admin setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Admin setEmail(String email) {
    this.email = email;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public Admin setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public Admin setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  @Override
  public String toString() {
    return "Admin{" +
        "id='" + id + '\'' +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", email='" + email + '\'' +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        '}';
  }
}
