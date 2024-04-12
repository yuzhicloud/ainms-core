package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.CountryStistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CountryStistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryStisticsRepository
  extends JpaRepository<CountryStistics, Long> {}
