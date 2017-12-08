package io.spm.parknshop.user.repository;

import io.spm.parknshop.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> getByUsername(String username);

  @Query(value = "SELECT id FROM user WHERE username = ?1", nativeQuery = true)
  Long getIdByUsername(String username);

  @Query(value = "SELECT id FROM user WHERE email = ?1", nativeQuery = true)
  Long getIdByEmail(String email);

  @Query(value = "SELECT id FROM user WHERE telephone = ?1", nativeQuery = true)
  Long getIdByTelephone(String telephone);
}
