package io.spm.parknshop.comment.service;

import io.spm.parknshop.comment.domain.Comment;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

  @Override
  public Mono<Comment> addComment(Comment comment) {
    return null;
  }

  @Override
  public Mono<Optional<Comment>> getById(Long id) {
    return null;
  }

  @Override
  public Flux<Comment> getCommentsByUser(Long userId) {
    return null;
  }

  @Override
  public Flux<Comment> getCommentsByProduct(Long productId) {
    return null;
  }

  @Override
  public Flux<Comment> getCommentsByParentId(Long parentId) {
    return null;
  }

  @Override
  public Mono<Long> deleteComment(Long id) {
    return null;
  }
}
