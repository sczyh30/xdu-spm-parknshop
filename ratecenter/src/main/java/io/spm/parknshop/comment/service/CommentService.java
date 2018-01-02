package io.spm.parknshop.comment.service;

import io.spm.parknshop.comment.domain.Comment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface CommentService {

  Mono<Comment> addComment(Comment comment);

  Mono<Comment> addReply(Comment comment);

  Mono<Optional<Comment>> getById(Long id);

  Flux<Comment> getCommentsByUser(Long userId);

  Flux<Comment> getCommentsByProduct(Long productId);

  Flux<Comment> getCommentsByParentId(Long parentId);

  Mono<Long> deleteComment(Long id);

}
