package com.yuzhi.ainms.core.service.dto;

import com.yuzhi.ainms.core.domain.PowerPlant;

public class PowerPlantWithProvinceDTO {
    private PowerPlant powerPlant;
    private Long powerPlantId;
    private Long provinceId;
    private String provinceName;
    private String powerPlantName;

    public PowerPlantWithProvinceDTO(PowerPlant powerPlant, String provinceName) {
        this.powerPlant = powerPlant;
        this.provinceName = provinceName;
    }

    public PowerPlantWithProvinceDTO(PowerPlant powerPlant, Long provinceId) {

        this.powerPlant = powerPlant;
        this.provinceId = provinceId;
    }

    // 构造器、getter和setter
    public PowerPlantWithProvinceDTO(Long powerPlantId, String powerPlantName, Long provinceId, String provinceName) {
        this.powerPlantId = powerPlantId;
        this.powerPlantName = powerPlantName;
        this.provinceId = provinceId;
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

    public Long getPowerPlantId() {
        return this.powerPlantId;
    }

    public String getPowerPlantName(){
        return this.powerPlantName;
    }

    public void setPowerPlantId(Long powerPlantId) {
        this.powerPlantId = powerPlantId;
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
            ", powerPlantId=" + powerPlantId +
            ", provinceId=" + provinceId +
            ", provinceName='" + provinceName + '\'' +
            '}';
    }
}
