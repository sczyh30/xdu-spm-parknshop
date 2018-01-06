package io.spm.parknshop.api.controller;

import io.spm.parknshop.query.service.impl.CustomerIndexDataService;
import io.spm.parknshop.query.vo.IndexPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class IndexApiController {

  @Autowired
  private CustomerIndexDataService customerIndexDataService;

  @GetMapping("/index")
  public Mono<IndexPage> apiIndex() {
    return customerIndexDataService.renderCustomerIndex();
  }
}
