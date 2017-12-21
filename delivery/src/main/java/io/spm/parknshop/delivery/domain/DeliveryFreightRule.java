package io.spm.parknshop.delivery.domain;

/**
 * Rule for calculating freight.
 *
 * @author Eric Zhao
 */
public class DeliveryFreightRule {

  private String rule;
  private double price;

  public String getRule() {
    return rule;
  }

  public DeliveryFreightRule setRule(String rule) {
    this.rule = rule;
    return this;
  }

  public double getPrice() {
    return price;
  }

  public DeliveryFreightRule setPrice(double price) {
    this.price = price;
    return this;
  }
}
