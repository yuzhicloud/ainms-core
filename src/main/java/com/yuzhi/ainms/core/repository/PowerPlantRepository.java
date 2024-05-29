package com.yuzhi.ainms.core.repository;

import com.yuzhi.ainms.core.domain.PowerPlant;
import com.yuzhi.ainms.core.service.dto.PowerPlantWithProvinceDTO;
import com.yuzhi.ainms.core.service.dto.ProvinceAccessPointCountDTO;
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

    @Query("SELECT new com.yuzhi.ainms.core.service.dto.PowerPlantWithProvinceDTO(pp.id, pp.powerPlantName, p.id, p.provinceName) " +
        "FROM PowerPlant pp JOIN pp.province p WHERE p.id = :provinceId")
    List<PowerPlantWithProvinceDTO> findAllByProvinceId(Long provinceId);

    @Query("SELECT new com.yuzhi.ainms.core.service.dto.ProvinceAccessPointCountDTO(p.province.id, p.province.provinceName, COUNT(ap)) " +
        "FROM AccessPoint ap " +
        "JOIN ap.group g " +
        "JOIN g.powerPlant p " +
        "GROUP BY p.province.id, p.province.provinceName")
    List<ProvinceAccessPointCountDTO> countAccessPointsByProvince();

    List<PowerPlant> findAllByPowerPlantNameContaining(String powerPlantName);

    List<PowerPlant> findAllByProvinceIdAndPowerPlantNameContaining(Long provinceId, String powerPlantName);

    List<PowerPlant> findAllByIdAndPowerPlantNameContaining(Long Id, String powerPlantName);
}
