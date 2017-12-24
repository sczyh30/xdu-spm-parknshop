package io.spm.parknshop.api.controller.user;

import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.delivery.domain.DeliveryAddress;
import io.spm.parknshop.delivery.service.DeliveryAddressService;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Eric Zhao
 */
@RestController
@RequestMapping("/api/v1/")
public class UserDeliveryAddressApiController {

  @Autowired
  private DeliveryAddressService deliveryAddressService;

  @GetMapping("/delivery_address/my_address")
  public Publisher<DeliveryAddress> apiGetAddressByUser(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
      .flatMapMany(userId -> deliveryAddressService.getByUserId(userId));
  }

  @PostMapping("/delivery_address/add_address")
  public Mono<DeliveryAddress> apiAddNewAddress(ServerWebExchange exchange, @RequestBody DeliveryAddress address) {
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> deliveryAddressService.addAddress(address.setUserId(userId)));
  }

  @PostMapping("/delivery_address/update_address/{id}")
  public Mono<DeliveryAddress> apiUpdateAddress(ServerWebExchange exchange, @PathVariable("id") Long id,
                                                     @RequestBody DeliveryAddress address) {
    // TODO: check user.
    return AuthUtils.getUserId(exchange)
      .flatMap(userId -> deliveryAddressService.updateAddress(id, address));
  }
}
