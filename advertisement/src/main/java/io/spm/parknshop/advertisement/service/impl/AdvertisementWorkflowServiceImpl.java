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
import io.spm.parknshop.apply.domain.ApplyProcessorRoles;
import io.spm.parknshop.apply.domain.ApplyResult;
import io.spm.parknshop.apply.domain.ApplyStatus;
import io.spm.parknshop.apply.repository.ApplyMetadataRepository;
import io.spm.parknshop.apply.service.ApplyDataService;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.DateUtils;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.common.util.JsonUtils;
import io.spm.parknshop.configcenter.service.GlobalConfigService;
import io.spm.parknshop.payment.domain.PaymentMethod;
import io.spm.parknshop.payment.domain.PaymentRecord;
import io.spm.parknshop.payment.domain.PaymentType;
import io.spm.parknshop.payment.service.PaymentService;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.store.service.StoreService;
import io.spm.parknshop.trade.domain.PaymentRedirectData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 * @author four
 */
@Service("adApplyService")
public class AdvertisementWorkflowServiceImpl implements AdvertisementWorkflowService {

  @Autowired
  private ApplyDataService applyDataService;
  @Autowired
  private AdvertisementService advertisementService;
  @Autowired
  private ProductService productService;
  @Autowired
  private PaymentService paymentService;
  @Autowired
  private StoreService storeService;
  @Autowired
  private GlobalConfigService globalConfigService;

  @Autowired
  private ApplyMetadataRepository applyMetadataRepository;

  @Autowired
  private AdApplyEventNotifier eventNotifier;
  @Autowired
  private AdApplyEventAggregator applyEventAggregator;

  @Override
  public Mono<Long> applyFor(String proposerId, AdvertisementDTO advertisement) {
    return checkApplyParamsThenExtractId(proposerId, advertisement)
      .flatMap(sellerId -> wrapAdvertisement(advertisement, sellerId))
      .flatMap(ad -> checkTargetExists(ad)
        .flatMap(v -> submitNewApply(proposerId, ad))
        .flatMap(t -> eventNotifier.doNotify(t.r2, t.r1)
          .map(v -> t.r1.getId()) // Return the apply id.
        )
      );
  }

  private Mono<Advertisement> wrapAdvertisement(AdvertisementDTO advertisementDTO, Long sellerId) {
    return globalConfigService.getAdPrice()
      .map(price -> calcAdTotalPrice(price, advertisementDTO.getAdType(), advertisementDTO.getStartDate(), advertisementDTO.getEndDate()))
      .map(price -> new Advertisement()
        .setAdType(advertisementDTO.getAdType())
        .setAdOwner(sellerId)
        .setAdTarget(advertisementDTO.getAdTarget())
        .setDescription(advertisementDTO.getDescription())
        .setAdPicUrl(advertisementDTO.getAdPicUrl())
        .setStartDate(advertisementDTO.getStartDate())
        .setEndDate(advertisementDTO.getEndDate())
        .setAdTotalPrice(price)
      );
  }

  private double calcAdTotalPrice(Tuple2<Double, Double> price, int adType, Date startDate, Date endDate) {
    long duration = ChronoUnit.DAYS.between(DateUtils.toLocalDate(startDate), DateUtils.toLocalDate(endDate)) + 1;
    if (adType == AdType.AD_PRODUCT) {
      return price.r1 * duration;
    } else {
      return price.r2 * duration;
    }
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
    if (!AdApplyType.isAdApply(applyType)) {
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
    if (Objects.isNull(applyResult)) {
      applyResult = new ApplyResult();
    }
    return applyDataService.performApplyTransform(applyId, AdApplyEventType.REJECT_APPLY, processorId, applyResult, applyEventAggregator);
  }

  @Override
  public Mono<Long> approveApply(Long applyId, String processorId, ApplyResult applyResult) {
    if (Objects.isNull(applyResult)) {
      applyResult = new ApplyResult();
    }
    return applyDataService.performApplyTransform(applyId, AdApplyEventType.APPROVE_APPLY, processorId, applyResult, applyEventAggregator)
      .flatMap(v -> createPayment(applyId));
  }

  private Mono<Long> createPayment(/*@NonNull*/ long applyId) {
    return applyDataService.getApplyById(applyId)
      .flatMap(apply -> paymentService.createPaymentRecord(parseAdvertisement(apply).getAdTotalPrice())
        .map(record -> apply.setApplyData(JsonUtils.toJson(wrapWithPayment(parseAdvertisement(apply), record))))
        .flatMap(e -> async(() -> applyMetadataRepository.save(e)))
        .map(e -> 0L)
      );
  }

  private Advertisement parseAdvertisement(Apply apply) {
    return JsonUtils.parse(apply.getApplyData(), Advertisement.class);
  }

  private Advertisement wrapWithPayment(Advertisement ad, PaymentRecord paymentRecord) {
    System.out.println(paymentRecord);
    return ad.setPaymentId(paymentRecord.getId());
  }

  @Override
  public Mono<Long> cancelApply(Long applyId, String processorId, ApplyResult applyResult) {
    if (Objects.isNull(applyResult)) {
      applyResult = new ApplyResult();
    }
    return applyDataService.performApplyTransform(applyId, AdApplyEventType.WITHDRAW_APPLY, processorId, applyResult, applyEventAggregator);
  }

  private Mono<Long> extractPaymentId(long applyId) {
    return applyDataService.getApplyById(applyId)
      .map(e -> parseAdvertisement(e).getPaymentId());
  }

  @Override
  public Mono<PaymentRedirectData> startPay(Long applyId) {
    return extractPaymentId(applyId)
      .flatMap(paymentId -> paymentService.startPayment(paymentId, PaymentMethod.ALIPAY, PaymentType.BUY_PAY));
  }

  @Override
  public Mono<Advertisement> finishPay(Long paymentId, String outerPaymentId) {
    return getAdApplyByPaymentId(paymentId)
      .flatMap(apply -> paymentService.finishPay(paymentId, outerPaymentId)
      .flatMap(v -> applyDataService.performApplyTransform(apply.getId(), AdApplyEventType.FINISH_PAY, apply.getProposerId(), new ApplyResult().setMessage("Payment ID: " + outerPaymentId), applyEventAggregator))
          .flatMap(v -> addAdvertisement(apply)
      ));
    /*return applyDataService.getApplyById(applyId)
      .flatMap(apply -> applyDataService.performApplyTransform(applyId, AdApplyEventType.FINISH_PAY, apply.getProposerId(), new ApplyResult().setMessage("Payment ID: " + outerPaymentId), applyEventAggregator)
        .flatMap(v -> addAdvertisement(apply))
      );*/
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

  private Mono<Long> checkApplyParamsThenExtractId(String proposerId, AdvertisementDTO advertisement) {
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
    if (advertisement.getStartDate().after(advertisement.getEndDate())) {
      return Mono.error(ExceptionUtils.invalidParam("Start date should not be later than end date"));
    }
    return ApplyProcessorRoles.checkSellerId(proposerId);
  }

  private Mono<Apply> getAdApplyByPaymentId(long paymentId) {
    return async(() -> applyMetadataRepository.getAdApplyByPaymentId(paymentId))
      .filter(Optional::isPresent)
      .map(Optional::get);
    //TODO: error handle.
  }
}
