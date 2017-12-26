package io.spm.parknshop.api.controller;

import io.spm.parknshop.inventory.domain.Inventory;
import io.spm.parknshop.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class InventoryApiController {

  @Autowired
  private InventoryService inventoryService;

  @GetMapping("/inventory/{id}")
  public Mono<Integer> apiGetInventory(@PathVariable("id") Long id) {
    return inventoryService.getInventoryAmount(id)
      .filter(Optional::isPresent)
      .map(Optional::get);
  }

  @PostMapping("/inventory/{id}")
  public Mono<Integer> apiModifyInventory(@PathVariable("id") Long id, @RequestBody Inventory inventory) {
    return inventoryService.modifyInventory(id, inventory);
  }
}
