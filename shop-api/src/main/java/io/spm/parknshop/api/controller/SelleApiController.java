package io.spm.parknshop.api.controller;

import io.spm.parknshop.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class SelleApiController {

  @Autowired
  SellerService sellerService;

}
