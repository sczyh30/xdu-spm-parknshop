package io.spm.parknshop.advertisement.domain.apply;

import java.util.Date;

/**
 * @author Eric Zhao
 */
public class AdvertisementDTO {

  private int adType;
  private String adOwner;
  private Long adTarget;

  private String description;
  private String adPicUrl;

  private Date startDate;
  private Date endDate;

  public int getAdType() {
    return adType;
  }

  public AdvertisementDTO setAdType(int adType) {
    this.adType = adType;
    return this;
  }

  public String getAdOwner() {
    return adOwner;
  }

  public AdvertisementDTO setAdOwner(String adOwner) {
    this.adOwner = adOwner;
    return this;
  }

  public Long getAdTarget() {
    return adTarget;
  }

  public AdvertisementDTO setAdTarget(Long adTarget) {
    this.adTarget = adTarget;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public AdvertisementDTO setDescription(String description) {
    this.description = description;
    return this;
  }

  public String getAdPicUrl() {
    return adPicUrl;
  }

  public AdvertisementDTO setAdPicUrl(String adPicUrl) {
    this.adPicUrl = adPicUrl;
    return this;
  }

  public Date getStartDate() {
    return startDate;
  }

  public AdvertisementDTO setStartDate(Date startDate) {
    this.startDate = startDate;
    return this;
  }

  public Date getEndDate() {
    return endDate;
  }

  public AdvertisementDTO setEndDate(Date endDate) {
    this.endDate = endDate;
    return this;
  }

  @Override
  public String toString() {
    return "AdvertisementDTO{" +
        "adType=" + adType +
        ", adOwner=" + adOwner +
        ", adTarget=" + adTarget +
        ", description='" + description + '\'' +
        ", adPicUrl='" + adPicUrl + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        '}';
  }
}
