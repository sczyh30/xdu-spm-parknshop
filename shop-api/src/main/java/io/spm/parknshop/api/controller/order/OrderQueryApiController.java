package io.spm.parknshop.api.controller.order;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.query.service.OrderQueryService;
import io.spm.parknshop.query.vo.OrderVO;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class OrderQueryApiController {

  @Autowired
  private OrderQueryService orderQueryService;

  @GetMapping("/order/query/simple/u")
  public Publisher<OrderVO> apiQueryOrderById(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(uid -> orderQueryService.queryOrdersByUser(uid));
  }

}
