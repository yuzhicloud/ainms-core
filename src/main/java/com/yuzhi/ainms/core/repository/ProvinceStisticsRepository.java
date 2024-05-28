package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.ProvinceStistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the ProvinceStistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvinceStisticsRepository
  extends JpaRepository<ProvinceStistics, Long> {
    @Query("select p from ProvinceStistics p where p.statisticDate>= ?1 and p.statisticDate<= ?2")
    Page<ProvinceStistics> findByDate(LocalDate start, LocalDate end, Pageable pageable);

    List<ProvinceStistics> findAllByStatisticDateBetween(LocalDate start, LocalDate end);
}
