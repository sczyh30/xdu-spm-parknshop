package io.spm.parknshop.comment.service;

import io.spm.parknshop.comment.domain.Comment;
import io.spm.parknshop.comment.domain.CommentDTO;
import io.spm.parknshop.comment.repository.CommentRepository;
import io.spm.parknshop.common.exception.ErrorConstants;
import io.spm.parknshop.common.exception.ServiceException;
import io.spm.parknshop.common.util.ExceptionUtils;
import io.spm.parknshop.order.domain.OrderStatus;
import io.spm.parknshop.order.service.OrderStatusService;
import io.spm.parknshop.product.domain.Product;
import io.spm.parknshop.product.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

import static io.spm.parknshop.common.async.ReactorAsyncWrapper.*;
import static io.spm.parknshop.common.exception.ErrorConstants.*;

/**
 * @author Xin Chu
 */
@Service
public class CommentServiceImpl implements CommentService {

  private static final int MAX_COMMENT_LENGTH = 1000;

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private ProductService productService;
  @Autowired
  private OrderStatusService orderStatusService;

  @Override
  public Mono<Comment> addCommentOrReply(/*@NonNull*/ Long userId, CommentDTO comment) {
    return checkThenWrap(userId, comment)
      .flatMap(this::addCommentInternal);
  }

  private Mono<Comment> checkThenWrap(/*@NonNull*/ Long userId, CommentDTO comment) {
    if (Objects.isNull(comment) || Objects.isNull(comment.getType())) {
      return Mono.error(ExceptionUtils.invalidParam("comment"));
    }
    switch (comment.getType()) {
      case "comment":
        return checkCommentThenWrapInternal(userId, comment);
      case "reply":
        return checkReplyThenWrapInternal(userId, comment);
      default:
        return Mono.error(ExceptionUtils.invalidParam("Not a valid new comment"));
    }
  }

  private Mono<Comment> checkCommentThenWrapInternal(/*@NonNull*/ Long userId, /*@NonNull*/ CommentDTO comment) {
    if (Objects.isNull(comment.getRate())) {
      comment.setRate(5);
    }
    if (comment.getRate() < 1 || comment.getRate() > 5) {
      return Mono.error(ExceptionUtils.invalidParam("Comment rate should be between 1 and 5"));
    }
    return checkCommentText(comment.getCommentText())
      .then(checkProductExists(comment.getProductId()))
      .then(checkAllowNewComment(userId, comment.getProductId()))
      .map(v -> new Comment().setRate(comment.getRate())
        .setProductId(comment.getProductId())
        .setCommentText(comment.getCommentText())
        .setParentId(ROOT_PARENT)
        .setUserId(userId)
      );
  }

  private Mono<?> checkAllowNewComment(/*@NonNull*/ Long userId, /*@NonNull*/ Long productId) {
    return checkIfCommentedBefore(userId, productId)
      .then(checkIfBoughtBefore(userId, productId));
  }

  private Mono<?> checkIfBoughtBefore(/*@NonNull*/ Long userId, /*@NonNull*/ Long productId) {
    return orderStatusService.getProductBuyStatusForUser(userId, productId)
      .flatMap(status -> {
        if (status == OrderStatus.COMPLETED) {
          return Mono.empty();
        } else {
          return Mono.error(new ServiceException(COMMENT_NOT_ALLOW_NOT_BUY, "You didn't buy the product or the order has not been completed"));
        }
      });
  }

  private Mono<?> checkIfCommentedBefore(/*@NonNull*/ Long userId, /*@NonNull*/ Long productId) {
    return async(() -> commentRepository.findCommentByUserForProduct(userId, productId))
      .flatMap(opt -> {
        if (opt.isPresent()) {
          return Mono.error(new ServiceException(DUPLICATE_COMMENT, "You've already commented on this product!"));
        } else {
          return Mono.just(productId);
        }
      });
  }

  private Mono<Comment> checkReplyThenWrapInternal(/*@NonNull*/ Long userId, /*@NonNull*/ CommentDTO comment) {
    if (Objects.nonNull(comment.getParentId()) || comment.getParentId() <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("Not a valid reply"));
    }
    return checkCommentText(comment.getCommentText())
      .flatMap(v -> productIdByParent(comment.getParentId()))
      .flatMap(this::checkProductExists)
      .map(product -> new Comment().setProductId(product.getId())
        .setCommentText(comment.getCommentText())
        .setParentId(comment.getParentId())
        .setUserId(userId)
      );
  }

  private Mono<Long> productIdByParent(long parentId) {
    return getById(parentId)
      .map(Comment::getProductId);
  }

  private Mono<?> checkCommentText(String text) {
    if (StringUtils.isEmpty(text)) {
      return Mono.error(ExceptionUtils.invalidParam("Comment text cannot be empty"));
    }
    if (text.length() > MAX_COMMENT_LENGTH) {
      return Mono.error(ExceptionUtils.invalidParam("Comment text max length exceeded: max length is " + MAX_COMMENT_LENGTH));
    }
    return Mono.just(text);
  }

  private Mono<Product> checkProductExists(Long productId) {
    return productService.getById(productId)
      .flatMap(productService::filterNormal);
  }

  private Mono<Comment> addCommentInternal(/*@NonNull*/ Comment comment) {
    return async(() -> commentRepository.save(comment));
  }

  @Override
  public Mono<Comment> getById(Long id) {
    if (Objects.isNull(id) || id <= 0) {
      return Mono.error(ExceptionUtils.invalidParam("id"));
    }
    return async(() -> commentRepository.findById(id))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .switchIfEmpty(Mono.error(new ServiceException(ErrorConstants.COMMENT_NOT_EXIST, "Comment does not exist")));
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
    return asyncExecute(() -> commentRepository.deleteComment(id));
  }
}
