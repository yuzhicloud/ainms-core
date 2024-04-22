package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.ProvinceStistics;
import org.hibernate.annotations.processing.HQL;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the ProvinceStistics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProvinceStisticsRepository
  extends JpaRepository<ProvinceStistics, Long> {
    @Query("select p from ProvinceStistics p where p.statisticDate= ?1")
    List<ProvinceStistics> findByDate(String date);
}
