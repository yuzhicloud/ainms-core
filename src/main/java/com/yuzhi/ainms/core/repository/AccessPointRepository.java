package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.AccessPoint;
import com.yuzhi.ainms.core.service.dto.PowerPlantAPStatisticsDTO;
import com.yuzhi.ainms.core.service.dto.ProvinceAPStatisticsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for the AccessPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessPointRepository extends JpaRepository<AccessPoint, Long> {
    @Query("SELECT ap FROM AccessPoint ap WHERE ap.group.id IN :groupIds")
    Page<AccessPoint> findByGroupIdsIn(@Param("groupIds") List<Long> groupIds, Pageable pageable);

    @Query("SELECT new com.yuzhi.ainms.core.service.dto.ProvinceAPStatisticsDTO(p.provinceName, COUNT(ap), " +
        "SUM(CASE WHEN ap.nestate = 11 THEN 1 ELSE 0 END), " +
        "SUM(CASE WHEN ap.nestate = 4 THEN 1 ELSE 0 END), " +
        "SUM(CASE WHEN ap.nestate NOT IN (11, 4) THEN 1 ELSE 0 END)) " +
        "FROM AccessPoint ap " +
        "JOIN ap.group g " +
        "JOIN g.powerPlant pp " +
        "JOIN pp.province p " +
        "GROUP BY p.provinceName")
    List<ProvinceAPStatisticsDTO> apStatisticsByProvince();

    @Query("SELECT new com.yuzhi.ainms.core.service.dto.PowerPlantAPStatisticsDTO(pp.id, pp.powerPlantName, p.provinceName, COUNT(ap), " +
        "SUM(CASE WHEN ap.nestate = 11 THEN 1 ELSE 0 END), " +
        "SUM(CASE WHEN ap.nestate = 4 THEN 1 ELSE 0 END), " +
        "SUM(CASE WHEN ap.nestate NOT IN (11, 4) THEN 1 ELSE 0 END)) " +
        "FROM AccessPoint ap " +
        "JOIN ap.group g " +
        "JOIN g.powerPlant pp " +
        "JOIN pp.province p " +
        "GROUP BY pp.id, pp.powerPlantName, p.provinceName")
    List<PowerPlantAPStatisticsDTO> apStatisticsByPowerPlant();
}
