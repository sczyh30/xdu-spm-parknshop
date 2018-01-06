package io.spm.parknshop.apply.repository;

import io.spm.parknshop.apply.domain.ApplyEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApplyEventRepository extends JpaRepository<ApplyEvent, Long> {

  List<ApplyEvent> getByApplyIdOrderById(long applyId);

  @Query(value = "SELECT * FROM apply_event WHERE apply_id = ?1 ORDER BY id LIMIT 1", nativeQuery = true)
  Optional<ApplyEvent> getSourceEvent(Long applyId);
}
