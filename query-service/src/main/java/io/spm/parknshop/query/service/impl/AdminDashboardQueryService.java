package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.common.auth.AuthRoles;
import io.spm.parknshop.configcenter.service.GlobalConfigService;
import io.spm.parknshop.order.repository.OrderRepository;
import io.spm.parknshop.query.vo.AdminDashboardVO;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class AdminDashboardQueryService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private GlobalConfigService globalConfigService;

  public Mono<AdminDashboardVO> getDashboardData() {
    return globalConfigService.getCommission()
    .flatMap(commission -> async(() -> {
      double profit = orderRepository.getAllSaleMoney() * commission / 100.0d;
      long customerCount = userRepository.countByUserType(AuthRoles.CUSTOMER);
      long sellerCount = userRepository.countByUserType(AuthRoles.SELLER);
      long orderCount = orderRepository.count();
      return new AdminDashboardVO().setTotalProfit(profit).setTotalCustomerAmount(customerCount)
        .setTotalSellerAmount(sellerCount).setTotalOrderAmount(orderCount);
    }));
  }
}
