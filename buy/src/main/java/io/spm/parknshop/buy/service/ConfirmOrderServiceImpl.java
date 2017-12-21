package io.spm.parknshop.buy.service;

import io.spm.parknshop.buy.domain.OrderPreview;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@Service
public class ConfirmOrderServiceImpl implements ConfirmOrderService {

  @Override
  public Mono<OrderPreview> previewOrder(Long userId) {
    return null;
  }
}
