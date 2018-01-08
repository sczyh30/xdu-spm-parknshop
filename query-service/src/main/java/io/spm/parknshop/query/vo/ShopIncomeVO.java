package io.spm.parknshop.query.vo;

import java.util.Date;
import java.util.List;

/**
 * @author Eric Zhao
 */
public class ShopIncomeVO {

  private Date start;
  private Date end;

  private int productSaleAmount;

  private double totalProfit;
  private double totalRawIncome;

  private List<OrderVO> orderList;

  public Date getStart() {
    return start;
  }

  public ShopIncomeVO setStart(Date start) {
    this.start = start;
    return this;
  }

  public Date getEnd() {
    return end;
  }

  public ShopIncomeVO setEnd(Date end) {
    this.end = end;
    return this;
  }

  public int getProductSaleAmount() {
    return productSaleAmount;
  }

  public ShopIncomeVO setProductSaleAmount(int productSaleAmount) {
    this.productSaleAmount = productSaleAmount;
    return this;
  }

  public double getTotalProfit() {
    return totalProfit;
  }

  public ShopIncomeVO setTotalProfit(double totalProfit) {
    this.totalProfit = totalProfit;
    return this;
  }

  public double getTotalRawIncome() {
    return totalRawIncome;
  }

  public ShopIncomeVO setTotalRawIncome(double totalRawIncome) {
    this.totalRawIncome = totalRawIncome;
    return this;
  }

  public List<OrderVO> getOrderList() {
    return orderList;
  }

  public ShopIncomeVO setOrderList(List<OrderVO> orderList) {
    this.orderList = orderList;
    return this;
  }
}
