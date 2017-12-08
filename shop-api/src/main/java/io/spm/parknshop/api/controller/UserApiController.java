package io.spm.parknshop.api.controller;

import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class UserApiController {

  @Autowired
  private UserService userService;

  @PostMapping("/user/login")
  public Mono<Boolean> apiUserLogin(@RequestBody User user) {
    return userService.login(user.getUsername(), user.getPassword());
  }

  @PutMapping("/user/{id}")
  public Mono<User> apiModifyUser(@PathVariable("id") Long id, @RequestBody User user) {
    return userService.modifyDetail(id, user);
  }

  @GetMapping("/user/{id}")
  public Mono<User> apiGetUserById(@PathVariable("id") Long id) {
    return userService.getUserById(id)
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @PostMapping("/user/register")
  public Mono<User> apiUserRegister(@RequestBody User user) {
    return userService.register(user);
  }
}
