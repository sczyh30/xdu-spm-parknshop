package io.spm.parknshop.query.service.impl;

import io.spm.parknshop.admin.domain.Admin;
import io.spm.parknshop.admin.service.AdminService;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.domain.ApplyProcessorRoles;
import io.spm.parknshop.apply.domain.ApplyRecordVO;
import io.spm.parknshop.apply.domain.ApplyVO;
import io.spm.parknshop.apply.service.ApplyDataService;
import io.spm.parknshop.common.auth.AuthRoles;
import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.query.service.ApplyQueryService;
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
  private AdminService adminService;

  @Override
  public Mono<ApplyVO> renderApplyView(Long applyId, String currentRole) {
    return applyDataService.checkAllowViewApply(applyId, currentRole)
      .flatMap(v -> applyDataService.getApplyById(applyId)
        .map(apply -> new ApplyVO().setApply(apply).setApplyId(applyId))
        .flatMap(this::wrapApplyRecords)
      );
  }

  private Mono<ApplyVO> wrapApplyRecords(/*@NonNull*/ ApplyVO applyVO) {
    long applyId = applyVO.getApplyId();
    return applyDataService.getEventStream(applyId)
      .flatMap(this::wrapEventWithRecord)
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
