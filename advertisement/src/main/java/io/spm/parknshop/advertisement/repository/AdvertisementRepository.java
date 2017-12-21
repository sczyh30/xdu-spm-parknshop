package io.spm.parknshop.advertisement.repository;

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

  List<Advertisement> getByAdTypeAndAdTarget(int type, long target);

  @Query(value = "SELECT count(*) FROM advertisement WHERE start_date >= ?1 AND end_date <= ?2", nativeQuery = true)
  int countBetweenDateRange(Date from, Date to);

  @Query(value = "SELECT count(*) FROM advertisement WHERE start_date >= ?1 AND end_date <= ?2", nativeQuery = true)
  List<Advertisement> getBetweenDateRange(Date from, Date to);
}
