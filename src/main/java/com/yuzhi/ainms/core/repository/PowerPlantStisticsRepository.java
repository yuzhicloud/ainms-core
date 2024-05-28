package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.PowerPlantStistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data JPA repository for the PowerPlantStistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerPlantStisticsRepository
  extends JpaRepository<PowerPlantStistics, Long> {

    @Query("select p from PowerPlantStistics p where p.statisticDate>= ?1 and p.statisticDate<=?2")
    Page<PowerPlantStistics> findByDate(LocalDate start, LocalDate end, Pageable pageable);

    List<PowerPlantStistics> findAllByStatisticDateBetween(LocalDate start, LocalDate end);
}
