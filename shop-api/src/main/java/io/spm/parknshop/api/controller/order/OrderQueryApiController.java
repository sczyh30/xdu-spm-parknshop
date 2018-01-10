package io.spm.parknshop.api.controller.order;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.query.service.OrderQueryService;
import io.spm.parknshop.query.vo.OrderVO;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class OrderQueryApiController {

  @Autowired
  private OrderQueryService orderQueryService;

  @GetMapping("/order/query/simple/u")
  public Publisher<OrderVO> apiQueryOrdersByUser(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(uid -> orderQueryService.queryOrdersByUser(uid));
  }

  @GetMapping("/order/query/simple/u/range")
  public Publisher<OrderVO> apiGetIncomeForShopBetween(@RequestParam(name = "from") Long from, @RequestParam(name = "to") Long to, ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
    .flatMapMany(userId -> orderQueryService.queryOrdersByUserBetween(userId, new Date(from), new Date(to)));
  }


  @GetMapping("/order/query/simple/u/daily")
  public Publisher<OrderVO> apiQueryOrdersByUserDaily(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(uid -> orderQueryService.queryOrdersByUserDaily(uid));
  }

  @GetMapping("/order/query/simple/u/weekly")
  public Publisher<OrderVO> apiQueryOrdersByUserWeekly(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(uid -> orderQueryService.queryOrdersByUserWeekly(uid));
  }

  @GetMapping("/order/query/simple/u/monthly")
  public Publisher<OrderVO> apiQueryOrdersByUserMonthly(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(uid -> orderQueryService.queryOrdersByUserMonthly(uid));
  }

  @GetMapping("/order/query/simple/u/yearly")
  public Publisher<OrderVO> apiQueryOrdersByUserYearly(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(uid -> orderQueryService.queryOrdersByUserYearly(uid));
  }

  @GetMapping("/order/query/simple/seller")
  public Publisher<OrderVO> apiQueryOrdersByCurrentSeller(ServerWebExchange exchange,
                                                          @RequestParam(value = "type", defaultValue = "all") String type) {
    switch (type) {
      case "finished":
        return AuthUtils.getSellerId(exchange)
          .flatMapMany(uid -> orderQueryService.queryFinishedOrdersBySeller(uid));
      case "all":
      default:
        return AuthUtils.getSellerId(exchange)
          .flatMapMany(uid -> orderQueryService.queryOrdersBySeller(uid));
    }
  }

  @GetMapping("/order/query/simple/store/{id}")
  public Publisher<OrderVO> apiQueryOrdersByStore(@PathVariable("id") Long id) {
    return orderQueryService.queryOrdersByStore(id);
  }

  @GetMapping("/order/query/detail/{id}")
  public Mono<OrderVO> apiQueryOrderById(ServerWebExchange exchange, @PathVariable("id") Long id) {
    return orderQueryService.queryOrderById(id);
  }

  @GetMapping("/order/query/simple/all")
  public Publisher<OrderVO> apiQueryAllOrders(ServerWebExchange exchange) {
    return orderQueryService.queryAllOrders();
  }

  @GetMapping("/order/query/simple/all/range")
  public Publisher<OrderVO> apiQueryOrdersBetween(@RequestParam(name = "from") Long from, @RequestParam(name = "to") Long to, ServerWebExchange exchange) {
    return orderQueryService.queryAllOrdersBetween(new Date(from), new Date(to));
  }


  @GetMapping("/order/query/simple/all/daily")
  public Publisher<OrderVO> apiQueryOrdersDaily(ServerWebExchange exchange) {
    return orderQueryService.queryAllOrdersDaily();
  }

  @GetMapping("/order/query/simple/all/weekly")
  public Publisher<OrderVO> apiQueryOrdersWeekly(ServerWebExchange exchange) {
    return orderQueryService.queryAllOrdersWeekly();
  }

  @GetMapping("/order/query/simple/all/monthly")
  public Publisher<OrderVO> apiQueryAllOrdersMonthly(ServerWebExchange exchange) {
    return orderQueryService.queryAllOrdersMonthly();
  }

  @GetMapping("/order/query/simple/all/yearly")
  public Publisher<OrderVO> apiQueryAllOrdersByYearly(ServerWebExchange exchange) {
    return orderQueryService.queryAllOrdersYearly();
  }

}
