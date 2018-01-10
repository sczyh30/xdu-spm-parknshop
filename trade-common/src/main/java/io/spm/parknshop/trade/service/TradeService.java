package io.spm.parknshop.trade.service;

import io.spm.parknshop.trade.domain.ConfirmOrderMessage;
import io.spm.parknshop.trade.domain.PaymentResult;
import io.spm.parknshop.trade.domain.SubmitOrderResult;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface TradeService {

  Mono<SubmitOrderResult> dispatchAndProcessOrder(ConfirmOrderMessage confirmOrderMessage);

  Mono<SubmitOrderResult> startPayForOrder(Long orderId);

  Mono<PaymentResult> cancelOrder(String proposer, Long orderId);
}
