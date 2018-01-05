package io.spm.parknshop.api.controller.admin;

import io.spm.parknshop.admin.service.DbBackupService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author four
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class AdminDbBackupApiController {

  @Autowired
  private DbBackupService dbBackupService;

  @GetMapping("/admin/db/backups")
  public Publisher<Long> apiGetBackups() {
    return dbBackupService.getBackups();
  }

  @PostMapping("/admin/db/recover")
  public Mono<Long> apiRecover(@RequestParam("timestamp") Long timestamp) {
    return dbBackupService.recover(timestamp);
  }

  @PostMapping("/admin/db/backup")
  public Mono<?> apiBackup() {
    return dbBackupService.backupDB();
  }
}
