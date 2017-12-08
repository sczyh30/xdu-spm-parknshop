package io.spm.parknshop.admin.repository;

import io.spm.parknshop.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepostitoy extends JpaRepository<Admin, String> {

  Optional<Admin> getByUsername(String username);

  @Query(value = "SELECT id FROM admin WHERE username=?1", nativeQuery = true)
  String getIdByUsername(String username);

}
