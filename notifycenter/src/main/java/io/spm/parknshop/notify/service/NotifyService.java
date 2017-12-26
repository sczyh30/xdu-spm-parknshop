package io.spm.parknshop.notify.service;

import io.spm.parknshop.notify.domain.NotifyMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class NotifyService {

  public Flux<NotifyMessage> getAllMessagesForUser(Long userId) {
    return Flux.empty();
  }

  public Flux<NotifyMessage> getAllMessagesForAdmin(Long adminId) {
    return Flux.empty();
  }

  public Mono<Long> batchPublishMessageForUser(List<Long> users) {
    return null;
  }

  public Mono<Long> batchPublishMessageForAdmin(List<Long> admins) {
    return null;
  }
}
