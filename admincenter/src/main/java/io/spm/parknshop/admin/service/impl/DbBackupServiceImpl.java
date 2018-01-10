package io.spm.parknshop.admin.service.impl;

import io.spm.parknshop.admin.service.DbBackupService;
import io.spm.parknshop.common.util.ExceptionUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Objects;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;

@Service
public class DbBackupServiceImpl implements DbBackupService {

  private static final String BACKUP_PATH = System.getProperty("user.home") + "/db_backup";
  private static final String BACKUP_PATH_PREFIX = System.getProperty("user.home") + "/db_backup/";

  private static final String BACKUP_FILE_PREFIX = "db_backup_";
  private static final String BACKUP_FILE_POSTFIX = ".sql";

  private String generateBackupFilename() {
    return BACKUP_FILE_PREFIX + System.currentTimeMillis() + BACKUP_FILE_POSTFIX;
  }

  private String generateBackupFilename(long timestamp) {
    return BACKUP_FILE_PREFIX + timestamp + BACKUP_FILE_POSTFIX;
  }

  private Long extractTimestamp(String filename) {
    return Long.valueOf(filename.replaceAll(BACKUP_FILE_POSTFIX, "").replaceAll(BACKUP_FILE_PREFIX, ""));
  }

  private Mono<?> checkExecuteResult(Process process) {
    return async(process::waitFor)
      .flatMap(returnValue -> {
          if (returnValue == 0) {
            return Mono.just(0);
          } else {
            return Mono.error(new IllegalStateException("Execute failed"));
          }
        }
      );
  }

  @Override
  public Mono<String> backupDB() {
    File backupDir = new File(BACKUP_PATH);
    if (!backupDir.exists()) {
      backupDir.mkdirs();
    }
    String commandPrefix = "mysqldump -uroot parknshop > ";
    String filename = generateBackupFilename();
    String command = commandPrefix + BACKUP_PATH_PREFIX + filename;
    return async(() -> Runtime.getRuntime().exec(new String[]{"sh", "-c", command}))
      .flatMap(this::checkExecuteResult)
      .map(v -> filename);
  }

  @Override
  public Flux<Long> getBackups() {
    File backupDir = new File(BACKUP_PATH);
    if (!backupDir.exists()) {
      backupDir.mkdirs();
    }
    if (backupDir.isDirectory()) {
      return Flux.fromArray(backupDir.list((d, name) -> name.startsWith(BACKUP_FILE_PREFIX)))
        .map(this::extractTimestamp);
    } else {
      return Flux.empty();
    }
  }

  @Override
  public Mono<Long> recover(Long timestamp) {
    if (Objects.isNull(timestamp) || timestamp < 0) {
      return Mono.error(ExceptionUtils.invalidParam("timestamp"));
    }
    String commandPrefix = "mysqldump -uroot parknshop < ";
    String filename = generateBackupFilename(timestamp);
    String command = commandPrefix + BACKUP_PATH_PREFIX + filename;
    return async(() -> Runtime.getRuntime().exec(new String[]{"sh", "-c", command}))
      .flatMap(this::checkExecuteResult)
      .map(v -> timestamp);
  }
}
