package io.spm.parknshop.apply.domain;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public class AdvertisementDO {

  private int adType;
  private String adOwner;
  private Long adTarget;

  private String description;
  private String adUrl;

  private Date startDate;
  private Date endDate;

  public int getAdType() {
    return adType;
  }

  public AdvertisementDO setAdType(int adType) {
    this.adType = adType;
    return this;
  }

  public String getAdOwner() {
    return adOwner;
  }

  public AdvertisementDO setAdOwner(String adOwner) {
    this.adOwner = adOwner;
    return this;
  }

  public Long getAdTarget() {
    return adTarget;
  }

  public AdvertisementDO setAdTarget(Long adTarget) {
    this.adTarget = adTarget;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public AdvertisementDO setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getAdUrl() {
    return adUrl;
  }

  public AdvertisementDO setAdUrl(String adUrl) {
    this.adUrl = adUrl;
    return this;
  }

  public Date getStartDate() {
    return startDate;
  }

  public AdvertisementDO setStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public Date getEndDate() {
    return endDate;
  }

  public AdvertisementDO setEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  @Override
  public String toString() {
    return "AdvertisementDO{" +
        "adType=" + adType +
        ", adOwner=" + adOwner +
        ", adTarget=" + adTarget +
        ", description='" + description + '\'' +
        ", adUrl='" + adUrl + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        '}';
  }
}
