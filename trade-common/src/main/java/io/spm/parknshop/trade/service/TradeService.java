package io.spm.parknshop.trade.service;

import io.spm.parknshop.trade.domain.ConfirmOrderMessage;
import io.spm.parknshop.trade.domain.ConfirmOrderResult;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface TradeService {

  Mono<ConfirmOrderResult> dispatchAndProcessOrder(ConfirmOrderMessage confirmOrderMessage);

}
