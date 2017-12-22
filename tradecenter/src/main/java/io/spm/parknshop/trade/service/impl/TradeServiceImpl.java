package io.spm.parknshop.trade.service.impl;

import io.spm.parknshop.trade.domain.ConfirmOrderMessage;
import io.spm.parknshop.trade.domain.ConfirmOrderResult;
import io.spm.parknshop.trade.service.TradeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@Service
public class TradeServiceImpl implements TradeService {

  @Override
  public Mono<ConfirmOrderResult> dispatchAndProcessOrder(ConfirmOrderMessage confirmOrderMessage) {
    return null;
  }
}
