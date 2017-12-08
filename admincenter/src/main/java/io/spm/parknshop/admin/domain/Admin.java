package io.spm.parknshop.admin.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
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
  private String id;

  private String username;
  private String password;

  private String email;
  private Date gmtCreate;
  private Date gmtModified;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public void setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
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
