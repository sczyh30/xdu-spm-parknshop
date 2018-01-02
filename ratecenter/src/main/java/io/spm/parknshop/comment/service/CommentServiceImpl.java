package io.spm.parknshop.comment.service;

import io.spm.parknshop.comment.domain.Comment;
import io.spm.parknshop.comment.repository.CommentRepository;
import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.async;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.asyncExecute;
import static io.spm.parknshop.common.async.ReactorAsyncWrapper.asyncIterable;
/**
 * @author  Xin Chu
 */

@Service
public class CommentServiceImpl implements CommentService {

  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private ProductService productService;

  @Override
  public Mono<Comment> addComment(Comment comment) {
    if (Objects.nonNull(comment.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return checkComment(comment)
        .flatMap(e -> async(() -> addCommentInternal(comment)));
  }

  @Override
  public Mono<Comment> addReply(Comment comment) {
    if (Objects.nonNull(comment.getId())) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return checkReply(comment)
        .flatMap(e -> async(() -> addCommentInternal(comment)));
  }

  private Mono<?> checkComment(Comment comment) {
    if (!isTextExists(comment)) {
      return Mono.error(ExceptionUtils.invalidParam("commentText"));
    }
    if (Objects.isNull(comment.getParentId()) || comment.getParentId() != 0){
      return Mono.error(ExceptionUtils.invalidParam("parentId"));
    }
    return checkProductExists(comment.getProductId())
        .flatMap(e -> checkCommentRate(comment));
  }

  private Mono<?> checkReply(Comment comment) {
    if (!isTextExists(comment)){
      return Mono.error(ExceptionUtils.invalidParam("commentText"));
    }
    return checkProductConsistence(comment)
        .flatMap(e -> checkProductExists(comment.getProductId()))
        .flatMap(e -> checkReplyRate(comment));
  }

  private Mono<?> checkProductConsistence(Comment comment) {
    if(Objects.isNull(comment.getParentId())){
      return Mono.error(ExceptionUtils.invalidParam("parentId"));
    }
    if(Objects.isNull(comment.getProductId())){
      return Mono.error(ExceptionUtils.invalidParam("productId"));
    }
    return getById(comment.getParentId())
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(Comment::getProductId)
        .filter(e -> e.equals(comment.getProductId()))
        .switchIfEmpty(Mono.error(ExceptionUtils.invalidParam("parentId or productId")));
  }

  private boolean isTextExists(final Comment comment) {
    return Optional.ofNullable(comment)
        .map(e -> comment.getCommentText())
        .isPresent();
  }

  private Mono<Product> checkProductExists(Long productId) {
    if(Objects.isNull(productId)){
      return Mono.error(ExceptionUtils.invalidParam("productId"));
    }
    return productService.getById(productId)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.PRODUCT_NOT_EXIST, "Product does not exist")));
  }

  private Mono<?> checkCommentRate(Comment comment) {
    if (Objects.isNull(comment.getRate())){
      comment.setRate(5);
    }
    else {
      if (comment.getRate() < 1 || comment.getRate() > 5){
        return Mono.error(ExceptionUtils.invalidParam("rate"));
      }
    }
    return Mono.just(comment);
  }

  private Mono<?> checkReplyRate(Comment comment) {
    if (Objects.isNull(comment.getRate())){
      comment.setRate(0);
    }
    return Mono.just(comment);
  }

  @Transactional
  protected Comment addCommentInternal(Comment comment) {
    return commentRepository.save(comment);
  }

  @Override
  public Mono<Optional<Comment>> getById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> commentRepository.findById(id));
  }

  @Override
  public Flux<Comment> getCommentsByUser(Long userId) {
    if (Objects.isNull(userId) || userId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("userId"));
    }
    return asyncIterable(() -> commentRepository.getByUserId(userId));
  }

  @Override
  public Flux<Comment> getCommentsByProduct(Long productId) {
    if (Objects.isNull(productId) || productId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("productId"));
    }
    return asyncIterable(() -> commentRepository.getByProductId(productId));
  }

  @Override
  public Flux<Comment> getCommentsByParentId(Long parentId) {
    if (Objects.isNull(parentId) || parentId <= 0) {
      return Flux.error(ExceptionUtils.invalidParam("parentId"));
    }
    return asyncIterable(() -> commentRepository.getByParentId(parentId));
  }

  @Override
  public Mono<Long> deleteComment(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return asyncExecute(() ->commentRepository.deleteByParent(id));
  }
}
