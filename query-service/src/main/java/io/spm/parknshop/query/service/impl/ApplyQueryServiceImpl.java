package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.domain.apply.AdApplyType;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.domain.ApplyProcessorRoles;
import io.spm.parknshop.apply.domain.ApplyRecordVO;
import io.spm.parknshop.apply.domain.ApplyVO;
import io.spm.parknshop.apply.service.ApplyDataService;
import io.spm.parknshop.common.auth.AuthRoles;
import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.JsonUtils;
import io.spm.parknshop.query.service.ApplyQueryService;
import io.spm.parknshop.seller.service.SellerUserService;
import io.spm.parknshop.store.domain.StoreDTO;
import io.spm.parknshop.store.service.StoreWorkflowService;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
@Service
public class ApplyQueryServiceImpl implements ApplyQueryService {

  @Autowired
  private ApplyDataService applyDataService;
  @Autowired
  private UserService userService;
  @Autowired
  private SellerUserService sellerUserService;
  @Autowired
  private AdminService adminService;

  @Autowired
  private AdPageQueryService adPageQueryService;

  @Override
  public Mono<ApplyVO> renderApplyView(Long applyId, String currentRole) {
    return applyDataService.checkAllowViewApply(applyId, currentRole)
      .flatMap(v -> applyDataService.getApplyById(applyId)
        .map(apply -> new ApplyVO().setApply(apply).setApplyId(applyId))
        .flatMap(this::wrapApplyRecords)
        .flatMap(this::wrapWithBizData)
      );
  }

  private Mono<ApplyVO> wrapWithBizData(/*@NonNull*/ ApplyVO applyVO) {
    switch (applyVO.getApply().getApplyType()) {
      case AdApplyType.APPLY_AD_PRODUCT:
        Long productId = JsonUtils.parse(applyVO.getApply().getApplyData(), Advertisement.class).getAdTarget();
        return adPageQueryService.getProductAdvertisement(productId)
          .map(applyVO::setBizData);
      case AdApplyType.APPLY_AD_SHOP:
        Long shopId = JsonUtils.parse(applyVO.getApply().getApplyData(), Advertisement.class).getAdTarget();
        return adPageQueryService.getShopAdvertisement(shopId)
          .map(applyVO::setBizData);
      case StoreWorkflowService.STORE_APPLY:
        Long sellerId = JsonUtils.parse(applyVO.getApply().getApplyData(), StoreDTO.class).getSellerId();
        return sellerUserService.getSellerById(sellerId)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .switchIfEmpty(Mono.just(new User().setUsername("Deleted Seller").setId(sellerId)))
          .map(applyVO::setBizData);
      default:
        return Mono.just(applyVO);
    }
  }

  private Mono<ApplyVO> wrapApplyRecords(/*@NonNull*/ ApplyVO applyVO) {
    long applyId = applyVO.getApplyId();
    return applyDataService.getEventStream(applyId)
      .concatMap(this::wrapEventWithRecord)
      .collectList()
      .map(applyVO::setRecords);
  }

  private Mono<ApplyRecordVO> wrapEventWithRecord(ApplyEvent applyEvent) {
    Tuple2<Integer, Long> processorDetail = ApplyProcessorRoles.getDetailed(applyEvent.getProcessorId());
    return retrieveProcessorName(processorDetail)
      .map(processorName -> new ApplyRecordVO().setApplyEventType(applyEvent.getApplyEventType())
        .setExtraInfo(applyEvent.getExtraInfo())
        .setProcessorId(processorDetail.r2)
        .setProcessorRole(processorDetail.r1)
        .setProcessorName(processorName)
        .setProcessTime(applyEvent.getGmtCreate())
      );
  }

  private Mono<String> retrieveProcessorName(Tuple2<Integer, Long> processorDetail) {
    switch (processorDetail.r1) {
      case AuthRoles.CUSTOMER:
      case AuthRoles.SELLER:
        return userService.getUserById(processorDetail.r2)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .map(User::getUsername)
          .switchIfEmpty(Mono.just("Deleted Account"));
      case AuthRoles.ADMIN:
        return adminService.getById(processorDetail.r2)
          .filter(Optional::isPresent)
          .map(Optional::get)
          .map(Admin::getUsername)
          .switchIfEmpty(Mono.just("Deleted Admin"));
      case AuthRoles.SYS_AUTO:
        return Mono.just("System Account");
      default:
        return Mono.error(new IllegalArgumentException("Unknown role"));
    }
  }
}
