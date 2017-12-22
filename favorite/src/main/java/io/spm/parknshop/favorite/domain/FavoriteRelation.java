package io.spm.parknshop.favorite.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Eric Zhao
 */
@Entity
@Table(name = "favorite")
public class FavoriteRelation {

  @Id
  @GeneratedValue
  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private Long userId;

  private Integer favoriteType;
  private Long targetId;

  public Long getId() {
    return id;
  }

  public FavoriteRelation setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public FavoriteRelation setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public FavoriteRelation setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public FavoriteRelation setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Integer getFavoriteType() {
    return favoriteType;
  }

  public FavoriteRelation setFavoriteType(Integer favoriteType) {
    this.favoriteType = favoriteType;
    return this;
  }

  public Long getTargetId() {
    return targetId;
  }

  public FavoriteRelation setTargetId(Long targetId) {
    this.targetId = targetId;
    return this;
  }
}
