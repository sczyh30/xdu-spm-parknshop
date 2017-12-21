package io.spm.parknshop.favorite.repository;

import io.spm.parknshop.favorite.domain.FavoriteRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<FavoriteRelation, Long> {

  List<FavoriteRelation> getByUserId(long userId);

  List<FavoriteRelation> getByFavoriteTypeAndTargetId(int type, long targetId);

  List<FavoriteRelation> getByUserIdAndFavoriteType(long userId, int type);

}
