package io.spm.parknshop.advertisement.service.impl;

import io.spm.parknshop.advertisement.domain.apply.AdApplyEventType;
import io.spm.parknshop.advertisement.domain.apply.AdApplyType;
import io.spm.parknshop.advertisement.domain.AdType;
import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.event.AdApplyEventAggregator;
import io.spm.parknshop.advertisement.event.AdApplyEventNotifier;
import io.spm.parknshop.advertisement.service.AdvertisementService;
import io.spm.parknshop.advertisement.service.AdvertisementWorkflowService;
import io.spm.parknshop.advertisement.domain.apply.AdvertisementDTO;
import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.domain.ApplyStatus;
import io.spm.parknshop.apply.service.ApplyDataService;
import io.spm.parknshop.apply.service.ApplyProcessService;
import io.spm.parknshop.apply.service.ApplyService;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.common.util.JsonUtils;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.store.service.StoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Optional;

import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 * @author four
 */
@Service("adApplyService")
public class AdvertisementWorkflowServiceImpl implements AdvertisementWorkflowService, ApplyService<AdvertisementDTO, Long>, ApplyProcessService {

  @Autowired
  private ApplyDataService applyDataService;
  @Autowired
  private AdvertisementService advertisementService;
  @Autowired
  private ProductService productService;
  @Autowired
  private StoreService storeService;

  @Autowired
  private AdApplyEventNotifier eventNotifier;
  @Autowired
  private AdApplyEventAggregator applyEventAggregator;

  @Override
  public Mono<Long> applyFor(String proposerId, AdvertisementDTO advertisement) {
    return checkApplyParams(proposerId, advertisement)
      .map(this::wrapAdvertisement)
      .flatMap(ad -> checkTargetExists(ad)
        .flatMap(v -> submitNewApply(proposerId, ad))
        .flatMap(t -> eventNotifier.doNotify(t.r2, t.r1)
          .map(v -> t.r1.getId()) // Return the apply id.
        )
      );
  }

  private Advertisement wrapAdvertisement(AdvertisementDTO advertisementDTO) {
    return new Advertisement()
      .setAdType(advertisementDTO.getAdType())
      .setAdOwner(advertisementDTO.getAdOwner())
      .setAdTarget(advertisementDTO.getAdTarget())
      .setDescription(advertisementDTO.getDescription())
      .setAdPicUrl(advertisementDTO.getAdPicUrl())
      .setStartDate(advertisementDTO.getStartDate())
      .setEndDate(advertisementDTO.getEndDate())
      .setAdTotalPrice(advertisementDTO.getAdType() == AdType.AD_PRODUCT ? 1000 : 500);
  }

  private Mono<?> checkTargetExists(Advertisement advertisement) {
    switch (advertisement.getAdType()) {
      case AdType.AD_PRODUCT:
        return checkProductExists(advertisement.getAdTarget());
      case AdType.AD_STORE:
        return checkStoreExists(advertisement.getAdTarget());
      default:
        return Mono.error(new ServiceException(AD_UNKNOWN_TYPE, "Unknown advertisement type"));
    }
  }

  private Mono<Tuple2<Apply, ApplyEvent>> submitNewApply(String proposerId, Advertisement advertisement) {
    int applyType = AdApplyType.fromAdType(advertisement.getAdType());
    if (AdApplyType.isAdApply(applyType)) {
      return Mono.error(new ServiceException(ILLEGAL_APPLY_TYPE, "Error apply type"));
    }
    Apply applyMetadata = new Apply().setApplyType(applyType)
      .setApplyData(JsonUtils.toJson(advertisement))
      .setProposerId(proposerId)
      .setStatus(ApplyStatus.NEW_APPLY);
    ApplyEvent applyEvent = new ApplyEvent().setApplyEventType(AdApplyEventType.SUBMIT_APPLY)
      .setProcessorId(proposerId);
    return applyDataService.saveNewApply(applyMetadata, applyEvent);
  }

  @Override
  public Mono<Long> rejectApply(Long applyId, String processorId, ApplyResult applyResult) {
    return applyDataService.performApplyTransform(applyId, AdApplyEventType.REJECT_APPLY, processorId, applyResult, applyEventAggregator);
  }

  @Override
  public Mono<Long> approveApply(Long applyId, String processorId, ApplyResult applyResult) {
    return applyDataService.performApplyTransform(applyId, AdApplyEventType.APPROVE_APPLY, processorId, applyResult, applyEventAggregator)
      .flatMap(e -> finishPay(applyId, 12233L)).map(e -> 0L); // TODO: For test
  }

  @Override
  public Mono<Long> cancelApply(Long applyId, String processorId) {
    return applyDataService.performApplyTransform(applyId, AdApplyEventType.WITHDRAW_APPLY, processorId, new ApplyResult(), applyEventAggregator);
  }

  @Override
  public Mono<String> startPay(String processorId, Long applyId) {
    // TODO: not implemented
    return Mono.empty();
  }

  @Override
  public Mono<Advertisement> finishPay(Long applyId, Long outerPaymentId) {
    return applyDataService.getApplyById(applyId)
      .flatMap(apply -> applyDataService.performApplyTransform(applyId, AdApplyEventType.FINISH_PAY, apply.getProposerId(), new ApplyResult().setMessage("Payment ID: " + outerPaymentId), applyEventAggregator)
        .flatMap(v -> addAdvertisement(apply))
      );
  }

  private Mono<Advertisement> addAdvertisement(Apply apply) {
    Advertisement advertisement = JsonUtils.parse(apply.getApplyData(), Advertisement.class)
      .setGmtModified(new Date())
      .setApplyId(apply.getId());
    return advertisementService.addNewAdvertisement(advertisement);
  }

  private Mono<?> checkProductExists(Long productId) {
    return productService.getById(productId);
  }

  private Mono<?> checkStoreExists(Long storeId) {
    return storeService.getById(storeId);
  }

  private Mono<AdvertisementDTO> checkApplyParams(String proposerId, AdvertisementDTO advertisement) {
    if (StringUtils.isEmpty(proposerId)) {
      return Mono.error(ExceptionUtils.invalidParam("proposerId"));
    }
    boolean valid = Optional.ofNullable(advertisement)
      .map(e -> advertisement.getAdTarget())
      .map(e -> advertisement.getAdPicUrl())
      .map(e -> advertisement.getDescription())
      .map(e -> advertisement.getStartDate())
      .map(e -> advertisement.getEndDate())
      .isPresent();
    if (!valid) {
      return Mono.error(ExceptionUtils.invalidParam("Invalid advertisement apply"));
    }
    return Mono.just(advertisement);
  }
}
