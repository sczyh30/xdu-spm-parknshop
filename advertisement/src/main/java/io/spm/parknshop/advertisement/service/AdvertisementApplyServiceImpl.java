package io.spm.parknshop.advertisement.service;

import io.spm.parknshop.advertisement.domain.AdType;
import io.spm.parknshop.advertisement.domain.Advertisement;
import io.spm.parknshop.advertisement.event.AdApplyEventNotifier;
import io.spm.parknshop.apply.domain.AdvertisementDO;
import io.spm.parknshop.apply.domain.Apply;
import io.spm.parknshop.apply.domain.ApplyEvent;
import io.spm.parknshop.apply.repository.ApplyEventRepository;
import io.spm.parknshop.apply.repository.ApplyMetadataRepository;
import io.spm.parknshop.apply.service.ApplyService;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.functional.Tuple2;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.service.ProductService;
import io.spm.parknshop.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Eric Zhao
 */
@Service
public class AdvertisementApplyServiceImpl implements ApplyService<AdvertisementDO, String> {

  @Autowired
  private ApplyMetadataRepository applyMetadataRepository;
  @Autowired
  private ApplyEventRepository applyEventRepository;

  @Autowired
  private ProductService productService;
  @Autowired
  private StoreService storeService;

  @Autowired
  private AdApplyEventNotifier eventNotifier;

  @Override
  public Mono<String> applyFor(Long proposerId, AdvertisementDO advertisement) {
    return checkApplyParams(proposerId, advertisement)
      .map(this::wrapAdvertisement)
      .flatMap(ad -> checkTargetExists(ad)
        .flatMap(v -> submitNewApply(ad))
        .flatMap(t -> eventNotifier.doNotify(t.r2, t.r1)
          .map(v -> t.r1.getId().toString()) // Return the apply id.
        )
      );
  }

  private Advertisement wrapAdvertisement(AdvertisementDO advertisementDO) {
    return new Advertisement(); // TODO
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

  private Mono<Tuple2<Apply, ApplyEvent>> submitNewApply(Advertisement advertisement) {
    return null;
  }

  private Mono<?> checkProductExists(Long productId) {
    return productService.getById(productId)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(PRODUCT_NOT_EXIST, "Target product does not exist")));
  }

  private Mono<?> checkStoreExists(Long storeId) {
    return storeService.getById(storeId)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(STORE_NOT_EXIST, "Target store does not exist")));
  }

  private Mono<AdvertisementDO> checkApplyParams(Long proposerId, AdvertisementDO advertisement) {
    if (Objects.isNull(proposerId) || proposerId <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    boolean valid = Optional.ofNullable(advertisement)
      .map(e -> advertisement.getAdOwner())
      .map(e -> advertisement.getAdTarget())
      .map(e -> advertisement.getAdUrl())
      .map(e -> advertisement.getDescription())
      .map(e -> advertisement.getStartDate())
      .map(e -> advertisement.getEndDate())
      .isPresent();
    if (!valid) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return Mono.just(advertisement);
  }
}
