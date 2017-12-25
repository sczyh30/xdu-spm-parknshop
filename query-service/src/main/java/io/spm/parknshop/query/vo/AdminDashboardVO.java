package io.spm.parknshop.query.vo;

public class AdminDashboardVO {

  private Double totalProfit;
  private Long totalCustomerAmount;
  private Long totalSellerAmount;
  private Long totalOrderAmount;

  public Double getTotalProfit() {
    return totalProfit;
  }

  public AdminDashboardVO setTotalProfit(Double totalProfit) {
    this.totalProfit = totalProfit;
    return this;
  }

  public Long getTotalCustomerAmount() {
    return totalCustomerAmount;
  }

  public AdminDashboardVO setTotalCustomerAmount(Long totalCustomerAmount) {
    this.totalCustomerAmount = totalCustomerAmount;
    return this;
  }

  public Long getTotalSellerAmount() {
    return totalSellerAmount;
  }

  public AdminDashboardVO setTotalSellerAmount(Long totalSellerAmount) {
    this.totalSellerAmount = totalSellerAmount;
    return this;
  }

  public Long getTotalOrderAmount() {
    return totalOrderAmount;
  }

  public AdminDashboardVO setTotalOrderAmount(Long totalOrderAmount) {
    this.totalOrderAmount = totalOrderAmount;
    return this;
  }
}
