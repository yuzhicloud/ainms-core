package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.PowerPlant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the PowerPlant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PowerPlantRepository extends JpaRepository<PowerPlant, Long> {
    // @Query("SELECT pp, pv.provinceName FROM PowerPlant pp JOIN pp.province pv")
    @Query("SELECT new com.yuzhi.ainms.core.repository.PowerPlantWithProvinceName(pp, pv.provinceName) FROM PowerPlant pp JOIN pp.province pv")
    List<PowerPlantWithProvinceName> findAllPowerPlantsWithProvinceNames();
}
