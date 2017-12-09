package io.spm.parknshop.io.spm.parknshop.service;

import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.store.domain.Store;
import io.spm.parknshop.user.domain.User;
import io.spm.parknshop.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.asyncIterable;

@Service
public class SellerServiceImpl implements SellerService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public Flux<User> searchSellerByKeyword(String keyword) {
    if(Objects.isNull(keyword) || "".equals(keyword)) {
      return Flux.error(ExceptionUtils.invalidParam("keyword"));
    }
    return asyncIterable(() -> userRepository.searchSellerByKeyword(keyword));
  }

  @Override
  public Mono<Boolean> applyStore(Store store) {
    return null;
  }
}
