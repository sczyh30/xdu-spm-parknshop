package io.spm.parknshop.query.vo;

import java.util.Date;
import java.util.List;

/**
 * @author Eric Zhao
 */
public class ShopSaleVO {

  private Date start;
  private Date end;

  private List<OrderVO> orderList;

  private double totalRawSaleIncome;

  public ShopSaleVO() {}

  public ShopSaleVO(Date start, Date end, List<OrderVO> orderList, double totalRawSaleIncome) {
    this.start = start;
    this.end = end;
    this.orderList = orderList;
    this.totalRawSaleIncome = totalRawSaleIncome;
  }

  public Date getStart() {
    return start;
  }

  public ShopSaleVO setStart(Date start) {
    this.start = start;
    return this;
  }

  public Date getEnd() {
    return end;
  }

  public ShopSaleVO setEnd(Date end) {
    this.end = end;
    return this;
  }

  public List<OrderVO> getOrderList() {
    return orderList;
  }

  public ShopSaleVO setOrderList(List<OrderVO> orderList) {
    this.orderList = orderList;
    return this;
  }

  public double getTotalRawSaleIncome() {
    return totalRawSaleIncome;
  }

  public ShopSaleVO setTotalRawSaleIncome(double totalRawSaleIncome) {
    this.totalRawSaleIncome = totalRawSaleIncome;
    return this;
  }
}
