package io.spm.parknshop.api.controller.shop;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.query.service.impl.RefundQueryService;
import io.spm.parknshop.query.vo.RefundVO;
import io.spm.parknshop.query.vo.apply.RefundApplyVO;
import io.spm.parknshop.refund.domain.RefundRecord;
import io.spm.parknshop.refund.service.RefundDataService;
import io.spm.parknshop.refund.service.RefundService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class RefundApiController {

  @Autowired
  private RefundQueryService refundQueryService;
  @Autowired
  private RefundService refundService;
  @Autowired
  private RefundDataService refundDataService;

  @GetMapping("/refund/render_new_refund")
  public Mono<RefundApplyVO> apiRenderRefundApply(@RequestParam("subOrderId") Long subOrderId, ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> refundQueryService.renderRefundApply(subOrderId));
  }

  @GetMapping("/refund/view_refund/{id}")
  public Mono<RefundVO> apiViewRefund(@PathVariable("id") Long id, ServerWebExchange exchange) {
    return refundQueryService.renderRefundView(id);
  }

  @GetMapping("/refund/view_refund_u")
  public Publisher<RefundVO> apiRefundListByCustomer(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(userId -> refundQueryService.getRefundByCustomer(userId));
  }

  @GetMapping("/refund/view_refund_store/{storeId}")
  public Publisher<RefundVO> apiRefundListByCustomer(@PathVariable("storeId") Long storeId, ServerWebExchange exchange) {
    return refundQueryService.getRefundByShop(storeId);
  }

  @PostMapping("/refund/op/new_refund")
  public Mono<RefundRecord> apiSubmitApplyForRefund(@RequestParam("subOrderId") Long subOrderId, @RequestBody String message, ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> refundDataService.createRefundRecord(subOrderId, message));
  }

  @PostMapping("/refund/op/approve/{refundId}")
  public Mono<Long> apiApproveRefundApply(@PathVariable("refundId") Long refundId, @RequestBody String responseMessage, ServerWebExchange exchange) {
    return AuthUtils.getSellerId(exchange)
      .flatMap(userId -> refundService.approveRefundRequest(refundId, responseMessage));
  }

  @PostMapping("/refund/op/reject/{refundId}")
  public Mono<Long> apiRejectRefundApply(@PathVariable("refundId") Long refundId, @RequestBody String responseMessage, ServerWebExchange exchange) {
    return AuthUtils.getSellerId(exchange)
      .flatMap(userId -> refundService.rejectRefundRequest(refundId, responseMessage));
  }

  @PostMapping("/refund/op/process_refund_tx/{refundId}")
  public Mono<RefundRecord> apiProcessRefundTransaction(@PathVariable("refundId") Long refundId, ServerWebExchange exchange) {
    return AuthUtils.getSellerId(exchange)
      .flatMap(userId -> refundService.startRefundProcess(refundId));
  }


  @PostMapping("/refund/op/cancel/{refundId}")
  public Mono<Long> apiCancelRefundApply(@PathVariable("refundId") Long refundId, ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> refundService.withdrawRefundRequest(refundId));
  }
}
