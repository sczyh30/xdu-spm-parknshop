package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.order.repository.OrderProductRepository;
import io.spm.parknshop.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class ShopIncomeService {

  @Autowired
  private OrderProductRepository orderProductRepository;
  @Autowired
  private OrderRepository orderRepository;


}
