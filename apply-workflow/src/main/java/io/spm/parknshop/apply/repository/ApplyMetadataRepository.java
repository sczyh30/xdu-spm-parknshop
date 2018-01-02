package io.spm.parknshop.apply.repository;

import io.spm.parknshop.apply.domain.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author Eric Zhao
 */
public interface ApplyMetadataRepository extends JpaRepository<Apply, Long> {

  @Query(value = "UPDATE apply_metadata SET gmt_modified = CURRENT_TIMESTAMP, status = ?2 WHERE id = ?1", nativeQuery = true)
  @Modifying
  @Transactional
  void updateStatus(long id, int status);

  List<Apply> getByProposerId(String proposerId);

  List<Apply> getByApplyType(int type);

  @Query(value = "SELECT * FROM apply_metadata WHERE proposer_id = :proposerId AND apply_type = :applyType AND status IN (:rg)", nativeQuery = true)
  List<Apply> getConditionalWithStatusRange(@Param("proposerId") String proposerId, @Param("applyType") int applyType,
                                            @Param("rg") String range);

  Optional<Apply> getById(Long id);

}
