package io.spm.parknshop.store.service;

import io.spm.parknshop.apply.service.ApplyProcessService;
import io.spm.parknshop.apply.service.ApplyService;
import io.spm.parknshop.store.domain.StoreDTO;

/**
 * Workflow service interface for shop apply.
 *
 * @author Eric Zhao
 */
public interface StoreWorkflowService extends ApplyService<StoreDTO, Long>, ApplyProcessService {

  /**
   * Apply type for store.
   */
  int STORE_APPLY = 1;

}
