package io.spm.parknshop.advertisement.domain;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public class Advertisement {

  private Long id;

  private Date gmtCreate;
  private Date gmtModified;

  private int adType;
  private Long adOwner;
  private Long adTarget;

  private String description;
  private String adUrl;
  private int adTotalPrice;

  private Date startDate;
  private Date endDate;

  private int status;

  public Long getId() {
    return id;
  }

  public Advertisement setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getGmtCreate() {
    return gmtCreate;
  }

  public Advertisement setGmtCreate(Date gmtCreate) {
    this.gmtCreate = gmtCreate;
    return this;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public Advertisement setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
    return this;
  }

  public int getAdType() {
    return adType;
  }

  public Advertisement setAdType(int adType) {
    this.adType = adType;
    return this;
  }

  public Long getAdOwner() {
    return adOwner;
  }

  public Advertisement setAdOwner(Long adOwner) {
    this.adOwner = adOwner;
    return this;
  }

  public Long getAdTarget() {
    return adTarget;
  }

  public Advertisement setAdTarget(Long adTarget) {
    this.adTarget = adTarget;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public Advertisement setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getAdUrl() {
    return adUrl;
  }

  public Advertisement setAdUrl(String adUrl) {
    this.adUrl = adUrl;
    return this;
  }

  public int getAdTotalPrice() {
    return adTotalPrice;
  }

  public Advertisement setAdTotalPrice(int adTotalPrice) {
    this.adTotalPrice = adTotalPrice;
    return this;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Advertisement setStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public Date getEndDate() {
    return endDate;
  }

  public Advertisement setEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  public int getStatus() {
    return status;
  }

  public Advertisement setStatus(int status) {
    this.status = status;
    return this;
  }
}
