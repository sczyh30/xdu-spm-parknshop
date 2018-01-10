package io.spm.parknshop.query.vo;

public class AppIncomeItem {

  private Long targetId;
  private Object targetObj;

  private int type;
  private double profit;
  private StoreVO store;

  public Long getTargetId() {
    return targetId;
  }

  public AppIncomeItem setTargetId(Long targetId) {
    this.targetId = targetId;
    return this;
  }

  public Object getTargetObj() {
    return targetObj;
  }

  public AppIncomeItem setTargetObj(Object targetObj) {
    this.targetObj = targetObj;
    return this;
  }

  public int getType() {
    return type;
  }

  public AppIncomeItem setType(int type) {
    this.type = type;
    return this;
  }

  public double getProfit() {
    return profit;
  }

  public AppIncomeItem setProfit(double profit) {
    this.profit = profit;
    return this;
  }

  public StoreVO getStore() {
    return store;
  }

  public AppIncomeItem setStore(StoreVO store) {
    this.store = store;
    return this;
  }
}
