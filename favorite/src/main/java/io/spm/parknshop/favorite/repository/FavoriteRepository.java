package io.spm.parknshop.favorite.repository;

import io.spm.parknshop.favorite.domain.FavoriteRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteRelation, Long> {

  List<FavoriteRelation> getByUserId(long userId);

  List<FavoriteRelation> getByFavoriteTypeAndTargetId(int type, long targetId);

  List<FavoriteRelation> getByUserIdAndFavoriteType(long userId, int type);

  @Query(value = "SELECT id FROM favorite WHERE user_id = ?1 AND favorite_type = ?2 AND target_id = ?3", nativeQuery = true)
  Optional<Long> checkUserFavorite(long userId, int type, long targetId);

  @Modifying
  @Transactional
  @Query(value = "DELETE FROM favorite WHERE user_id = ?1 AND favorite_type = ?2 AND target_id = ?3", nativeQuery = true)
  void cancelFavorite(long userId, int type, long targetId);

}
