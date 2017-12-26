package io.spm.parknshop.apply.repository;

import io.spm.parknshop.apply.domain.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface ApplyMetadataRepository extends JpaRepository<Apply, Long> {

  @Query(value = "UPDATE apply_metadata SET gmt_modified = CURRENT_TIMESTAMP, status = ?2 WHERE id = ?1", nativeQuery = true)
  @Modifying
  void updateStatus(long id, int status);

  List<Apply> getByProposerId(long proposerId);

  List<Apply> getByApplyType(int type);

  Optional<Apply> getById(Long id);

}
