package com.yuzhi.ainms.core.service.dto;

import com.yuzhi.ainms.core.domain.PowerPlant;

public class PowerPlantWithProvinceDTO {
    private PowerPlant powerPlant;
    private String provinceName;

    public PowerPlantWithProvinceDTO(PowerPlant powerPlant, String provinceName) {
        this.powerPlant = powerPlant;
        this.provinceName = provinceName;
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

    @Override
    public String toString() {
        return "PowerPlantWithProvinceDTO{" +
            "powerPlant=" + powerPlant +
            ", provinceName='" + provinceName + '\'' +
            '}';
    }
}
