package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.PowerPlantStistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PowerPlantStistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerPlantStisticsRepository
  extends JpaRepository<PowerPlantStistics, Long> {}
