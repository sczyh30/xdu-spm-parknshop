package io.spm.parknshop.query.vo;

public class ShopSaleVO {

  private double commission;
  private double totalSale;
  private double totalProfit;

  public double getCommission() {
    return commission;
  }

  public ShopSaleVO setCommission(double commission) {
    this.commission = commission;
    return this;
  }

  public double getTotalSale() {
    return totalSale;
  }

  public ShopSaleVO setTotalSale(double totalSale) {
    this.totalSale = totalSale;
    return this;
  }

  public double getTotalProfit() {
    return totalProfit;
  }

  public ShopSaleVO setTotalProfit(double totalProfit) {
    this.totalProfit = totalProfit;
    return this;
  }
}
