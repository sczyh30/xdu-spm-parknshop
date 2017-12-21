package io.spm.parknshop.apply.repository;

import io.spm.parknshop.apply.domain.Apply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Eric Zhao
 */
public interface ApplyMetadataRepository extends JpaRepository<Apply, Long> {

  List<Apply> getByProposerId(long proposerId);

  List<Apply> getByApplyType(int type);

  Apply getById(Long id);

}
