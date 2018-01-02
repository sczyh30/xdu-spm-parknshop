package io.spm.parknshop.buy.service;

import io.spm.parknshop.buy.domain.ConfirmOrderDO;
import io.spm.parknshop.trade.domain.SubmitOrderResult;
import io.spm.parknshop.trade.domain.OrderPreview;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface ConfirmOrderService {

  Mono<OrderPreview> previewOrder(Long userId);

  Mono<SubmitOrderResult> submitOrder(Long userId, ConfirmOrderDO requestDO);

}
