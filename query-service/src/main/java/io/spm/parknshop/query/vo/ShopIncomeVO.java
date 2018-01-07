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

  private double totalIncome;
  private double totalIncomeWithoutCommission;

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

  public double getTotalIncome() {
    return totalIncome;
  }

  public ShopIncomeVO setTotalIncome(double totalIncome) {
    this.totalIncome = totalIncome;
    return this;
  }

  public double getTotalIncomeWithoutCommission() {
    return totalIncomeWithoutCommission;
  }

  public ShopIncomeVO setTotalIncomeWithoutCommission(double totalIncomeWithoutCommission) {
    this.totalIncomeWithoutCommission = totalIncomeWithoutCommission;
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
