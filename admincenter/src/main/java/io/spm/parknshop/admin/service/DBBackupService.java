package io.spm.parknshop.admin.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DBBackupService {
  Mono<String> backupDB();

  Flux<String> getBackups();

  Mono<String> recover(String fileName);
}
