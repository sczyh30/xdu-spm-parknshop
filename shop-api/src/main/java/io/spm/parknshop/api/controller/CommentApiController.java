package io.spm.parknshop.api.controller;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import io.spm.parknshop.api.util.AuthUtils;
import io.spm.parknshop.comment.domain.Comment;
import io.spm.parknshop.comment.service.CommentService;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Xin Chu
 */

@RestController
@RequestMapping("/api/v1/")
public class CommentApiController {

  @Autowired
  private CommentService commentService;

  @PostMapping("/comment/add_comment")
  public Mono<Comment> apiAddComment( ServerWebExchange exchange,@RequestBody Comment comment) {
    return AuthUtils.getUserId(exchange)
        .flatMap(userId->commentService.addComment(comment.setUserId(userId)));
  }

  @PostMapping("/comment/add_reply")
  public Mono<Comment> apiAddReply(ServerWebExchange exchange,@RequestBody Comment comment) {
    return AuthUtils.getUserId(exchange)
        .flatMap(userId->commentService.addReply(comment.setUserId(userId)));
  }

  @GetMapping("/comment/{id}")
  public Mono<Comment> apiGetComment(ServerWebExchange exchange,@PathVariable("id") Long id) {
    return AuthUtils.getUserId(exchange)
        .flatMap(userId->commentService.getById(id))
        .filter(Optional::isPresent)
        .map(Optional::get);
  }

  @GetMapping("/comment/by_product/{productId}")
  public Publisher<Comment> apiGetByProduct(ServerWebExchange exchange,@PathVariable("productId") Long productId) {
    return AuthUtils.getUserId(exchange)
        .flatMapMany(userId->commentService.getCommentsByProduct(productId));
  }

  @GetMapping("/comment/by_parent/{parentId}")
  public Publisher<Comment> apiGetByParentId(ServerWebExchange exchange,@PathVariable("parentId") Long parentId) {
    return AuthUtils.getUserId(exchange)
        .flatMapMany(userId->commentService.getCommentsByParentId(parentId));
  }

  @GetMapping("/comment/by_user")
  public Publisher<Comment> apiGetByUserId(ServerWebExchange exchange) {
    return AuthUtils.getUserId(exchange)
        .flatMapMany(commentService::getCommentsByUser);
  }

  @DeleteMapping("/comment/{id}")
  public Mono<Long> apiDeleteComment(ServerWebExchange exchange,@PathVariable("id") Long id) {
    return AuthUtils.getUserId(exchange)
        .flatMap(userId->commentService.deleteComment(id));
  }
}
