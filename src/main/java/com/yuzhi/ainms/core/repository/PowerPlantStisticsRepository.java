package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.PowerPlantStistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the PowerPlantStistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerPlantStisticsRepository
  extends JpaRepository<PowerPlantStistics, Long> {

    @Query("select p from PowerPlantStistics p where p.statisticDate= ?1")
    List<PowerPlantStistics> findByDate(String date);
}
