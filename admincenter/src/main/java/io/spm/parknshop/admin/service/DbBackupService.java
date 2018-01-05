package io.spm.parknshop.admin.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DbBackupService {

  Mono<String> backupDB();

  Flux<Long> getBackups();

  Mono<Long> recover(Long timestamp);
}
