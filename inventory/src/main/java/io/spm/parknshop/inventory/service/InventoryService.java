package io.spm.parknshop.inventory.service;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.inventory.domain.Inventory;
import io.spm.parknshop.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;

@Service
public class InventoryService {

  @Autowired
  private InventoryRepository inventoryRepository;

  public Mono<Optional<Integer>> getInventoryAmount(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> inventoryRepository.getAmountById(id));
  }

  public Mono<Integer> modifyInventory(Long id, Inventory inventory) {
    return checkInventory(id, inventory)
      .flatMap(v -> async(() -> inventoryRepository.save(inventory.setGmtModified(new Date())))
      .map(e -> inventory.getAmount())
      );
  }

  private Mono<Integer> checkInventory(Long id, Inventory inventory) {
    if (!Optional.ofNullable(inventory).map(e -> inventory.getId()).map(e -> inventory.getAmount()).isPresent()) {
      return Mono.error(ExceptionUtils.invalidParam("inventory"));
    }
    if (inventory.getId() <= 0 || !inventory.getId().equals(id)) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    if (inventory.getAmount() < 0) {
      return Mono.error(ExceptionUtils.invalidParam("negative amount"));
    }
    return Mono.just(inventory.getAmount());
  }
}
