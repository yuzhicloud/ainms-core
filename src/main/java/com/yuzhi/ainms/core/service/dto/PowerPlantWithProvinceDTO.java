package com.yuzhi.ainms.core.service.dto;

import com.yuzhi.ainms.core.domain.PowerPlant;

public class PowerPlantWithProvinceDTO {
    private PowerPlant powerPlant;
    private Long provinceId;
    private String provinceName;

    public PowerPlantWithProvinceDTO(PowerPlant powerPlant, String provinceName) {
        this.powerPlant = powerPlant;
        this.provinceName = provinceName;
    }

    public PowerPlantWithProvinceDTO(PowerPlant powerPlant, Long provinceId) {
        this.powerPlant = powerPlant;
        this.provinceId = provinceId;
    }

    public PowerPlant getPowerPlant() {
        return powerPlant;
    }

    public void setPowerPlant(PowerPlant powerPlant) {
        this.powerPlant = powerPlant;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "PowerPlantWithProvinceDTO{" +
            "powerPlant=" + powerPlant +
            ", provinceId=" + provinceId +
            ", provinceName='" + provinceName + '\'' +
            '}';
    }
}
