package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.PowerPlant;
import com.yuzhi.ainms.core.service.dto.PowerPlantWithProvinceDTO;
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
    @Query("SELECT new com.yuzhi.ainms.core.service.dto.PowerPlantWithProvinceDTO(pp, pv.provinceName) FROM PowerPlant pp JOIN pp.province pv")
    List<PowerPlantWithProvinceDTO> findAllPowerPlantsWithProvinceNames();

    List<PowerPlantWithProvinceDTO> findAllByProvinceId(Long provinceId);
}
