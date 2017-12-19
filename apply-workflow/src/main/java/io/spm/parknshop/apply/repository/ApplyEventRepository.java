package io.spm.parknshop.apply.repository;

import io.spm.parknshop.apply.domain.ApplyEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplyEventRepository extends JpaRepository<ApplyEvent, Long> {

  List<ApplyEvent> getByApplyId(long applyId);
}
