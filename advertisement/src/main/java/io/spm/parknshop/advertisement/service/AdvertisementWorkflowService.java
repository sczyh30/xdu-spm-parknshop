package io.spm.parknshop.advertisement.service;

import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.domain.apply.AdvertisementDTO;
import io.spm.parknshop.apply.service.ApplyProcessService;
import io.spm.parknshop.apply.service.ApplyService;
import io.spm.parknshop.trade.domain.PaymentRedirectData;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
public interface AdvertisementWorkflowService extends ApplyService<AdvertisementDTO, Long>, ApplyProcessService {

  Mono<PaymentRedirectData> startPay(Long applyId);

  Mono<Advertisement> finishPay(String paymentId, String outerPaymentId);
}
