package io.spm.parknshop.advertisement.service;

import io.spm.parknshop.advertisement.domain.Advertisement;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface AdvertisementWorkflowService {

  Mono<String> startPay(String processorId, Long applyId);

  Mono<Advertisement> finishPay(Long applyId, Long outerPaymentId);
}
