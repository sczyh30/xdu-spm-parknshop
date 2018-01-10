package io.spm.parknshop.seller.service.impl;

import io.spm.parknshop.seller.service.SellerService;
import io.spm.parknshop.store.domain.StoreDTO;
import io.spm.parknshop.store.service.StoreWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Biz service implementation of {@link SellerService}.
 *
 * @author Eric Zhao
 * @author four
 */
@Service
public class SellerServiceImpl implements SellerService {

  @Autowired
  private StoreWorkflowService storeWorkflowService;

  @Override
  public Mono<Long> applyStore(String sellerProposer, StoreDTO storeDTO) {
    return storeWorkflowService.applyFor(sellerProposer, storeDTO);
  }
}
