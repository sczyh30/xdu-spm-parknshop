package io.spm.parknshop.advertisement.repository;

import io.spm.parknshop.advertisement.domain.AdType;
import io.spm.parknshop.advertisement.domain.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @author Eric Zhao
 */
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

  List<Advertisement> getByAdOwner(long owner);

  @Query(value = "SELECT * FROM advertisement ORDER BY id DESC", nativeQuery = true)
  List<Advertisement> getAll();

  List<Advertisement> getByAdTypeAndAdTarget(int type, long target);

  @Query(value = "SELECT count(*) FROM advertisement WHERE start_date >= ?1 AND end_date <= ?2", nativeQuery = true)
  int countBetweenDateRange(Date from, Date to);

  @Query(value = "SELECT * FROM advertisement WHERE start_date >= ?1 AND end_date <= ?2", nativeQuery = true)
  List<Advertisement> getBetweenDateRange(Date from, Date to);

  @Query(value = "SELECT a.* FROM advertisement a, product b WHERE a.start_date <= now() AND a.end_date >= now() AND a.ad_type = " + AdType.AD_PRODUCT + " AND a.ad_target = b.id AND b.status = 0 LIMIT 10", nativeQuery = true)
  List<Advertisement> getPresentProductAd();

  @Query(value = "SELECT * FROM advertisement WHERE start_date <= now() AND end_date >= now() AND ad_type = " + AdType.AD_STORE + " LIMIT 5", nativeQuery = true)
  List<Advertisement> getPresentShopAd();
}
