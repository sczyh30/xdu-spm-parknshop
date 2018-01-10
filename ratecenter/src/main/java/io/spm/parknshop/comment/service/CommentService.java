package io.spm.parknshop.comment.service;

import io.spm.parknshop.comment.domain.Comment;
import io.spm.parknshop.comment.domain.CommentDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface CommentService {

  long ROOT_PARENT = 0L;

  Mono<Comment> addCommentOrReply(Long userId, CommentDTO comment);

  Mono<Boolean> canComment(Long userId, Long productId);

  Mono<Comment> getById(Long id);

  Flux<Comment> getCommentsByUser(Long userId);

  Flux<Comment> getCommentsByProduct(Long productId);

  Flux<Comment> getCommentsByParentId(Long parentId);

  Mono<Long> deleteComment(Long id);

}
