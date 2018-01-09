package io.spm.parknshop.user.repository;

import io.spm.parknshop.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

  long countByUserType(int userType);

  @Query(value = "SELECT * FROM user WHERE username = ?1 AND user_type = 0", nativeQuery = true)
  Optional<User> getCustomerByUsername(String username);

  @Query(value = "SELECT * FROM user WHERE username = ?1 AND user_type = 1", nativeQuery = true)
  Optional<User> getSellerByUsername(String username);

  @Query(value = "SELECT * FROM user WHERE id = ?1 AND user_type = 1", nativeQuery = true)
  Optional<User> getSellerById(long sellerId);

  List<User> getByUserType(int userType);

  @Query(value = "SELECT id FROM user WHERE username = ?1", nativeQuery = true)
  Long getIdByUsername(String username);

  @Query(value = "SELECT id FROM user WHERE email = ?1", nativeQuery = true)
  Long getIdByEmail(String email);

  @Query(value = "SELECT id FROM user WHERE telephone = ?1", nativeQuery = true)
  Long getIdByTelephone(String telephone);

  @Query(value = "SELECT * FROM user WHERE user.user_type = 0 AND user.username LIKE CONCAT('%',:keyword,'%') \n#pageable \n",
      countQuery = "SELECT count(*) FROM user WHERE user.user_type = 0 AND user.username LIKE CONCAT('%',:keyword,'%')", nativeQuery = true)
  Page<User> searchCustomerByKeyword(@Param("keyword") String keyword, Pageable pageable);

  @Query(value = "SELECT * FROM user WHERE user.user_type = 1 AND user.username LIKE CONCAT('%',:keyword,'%')", nativeQuery = true)
  List<User> searchSellerByKeyword(@Param("keyword") String keyword);

  @Query(value = "UPDATE user SET user_status=?1, gmt_modified=CURRENT_TIMESTAMP WHERE id=?2", nativeQuery = true)
  @Modifying
  @Transactional
  void modifyStatus(int status, long id);

  @Query(value = "UPDATE store SET status=?1, gmt_modified=CURRENT_TIMESTAMP WHERE seller_id=?2", nativeQuery = true)
  @Modifying
  @Transactional
  void modifyStoreStatusBySellerId(int status, long id);

  @Query(value = "UPDATE user SET password=?1, gmt_modified=CURRENT_TIMESTAMP WHERE id=?2", nativeQuery = true)
  @Modifying
  @Transactional
  void modifyPassword(String encrypted, long id);
}
