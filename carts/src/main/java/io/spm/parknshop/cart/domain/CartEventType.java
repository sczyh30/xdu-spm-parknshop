package io.spm.parknshop.cart.domain;

public final class CartEventType {
  public static final int ADD_CART = 1;
  public static final int UPDATE_AMOUNT = 5;
  public static final int DECREASE_CART = 2;
  public static final int REMOVE = 3;
  public static final int CHECK = 4; // Do check/uncheck

  private CartEventType() {}
}
