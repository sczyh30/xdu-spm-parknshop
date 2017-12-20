package io.spm.parknshop.comment.repository;

import io.spm.parknshop.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Eric Zhao
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> getByProductId(long productId);

  List<Comment> getByParentId(long parentId);

  List<Comment> getByUserId(long userId);
}
