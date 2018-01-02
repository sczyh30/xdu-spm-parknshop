package io.spm.parknshop.comment.repository;

import io.spm.parknshop.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Eric Zhao
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

  List<Comment> getByProductId(long productId);

  List<Comment> getByParentId(long parentId);

  List<Comment> getByUserId(long userId);

  @Query(value = "DELETE FROM comment WHERE id = ?1 OR parent_id = ?1", nativeQuery = true)
  @Modifying
  @Transactional
  void deleteByParent(long parentId);
}
