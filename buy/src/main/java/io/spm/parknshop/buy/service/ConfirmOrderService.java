package io.spm.parknshop.buy.service;

import io.spm.parknshop.buy.domain.ConfirmOrderDO;
import io.spm.parknshop.buy.domain.ConfirmOrderResult;
import io.spm.parknshop.buy.domain.OrderPreview;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface ConfirmOrderService {

  Mono<OrderPreview> previewOrder(Long userId);

  Mono<ConfirmOrderResult> confirmOrder(Long userId, ConfirmOrderDO requestDO);

}
