package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.AccessPointGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the AccessPointGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccessPointGroupRepository extends JpaRepository<AccessPointGroup, Long> {
    List<AccessPointGroup> findByPowerPlantId(Long powerPlantId);
    List<AccessPointGroup> findByPowerPlantIdIn(List<Long> powerPlantIds);
}
