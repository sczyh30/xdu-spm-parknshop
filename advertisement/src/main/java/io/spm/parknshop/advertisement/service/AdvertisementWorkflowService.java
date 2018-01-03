package io.spm.parknshop.advertisement.service;

import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.domain.apply.AdvertisementDTO;
import io.spm.parknshop.apply.service.ApplyProcessService;
import io.spm.parknshop.apply.service.ApplyService;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface AdvertisementWorkflowService extends ApplyService<AdvertisementDTO, Long>, ApplyProcessService {

  Mono<String> startPay(String processorId, Long applyId);

  Mono<Advertisement> finishPay(Long applyId, Long outerPaymentId);
}
