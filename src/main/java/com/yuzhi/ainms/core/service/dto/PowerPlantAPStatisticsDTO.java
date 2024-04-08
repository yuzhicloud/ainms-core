package com.yuzhi.ainms.core.service.dto;

public class PowerPlantAPStatisticsDTO {
    private Long totalAPs;
    private Long standByAPCount;
    private Long offlineAPCount;
    private Long otherAPCount;
    private Long powerPlantId;
    private String powerPlantName;
    private String provinceName;

    public PowerPlantAPStatisticsDTO(Long powerPlantId,
                                     String powerPlantName,
                                     String provinceName,
                                     Long totalAPs,
                                     Long standByAPCount,
                                     Long offlineAPCount,
                                     Long otherAPCount
                                     ) {
        this.provinceName = provinceName;
        this.powerPlantName = powerPlantName;
        this.powerPlantId = powerPlantId;
        this.otherAPCount = otherAPCount;
        this.offlineAPCount = offlineAPCount;
        this.standByAPCount = standByAPCount;
        this.totalAPs = totalAPs;
    }

    public Long getTotalAPs() {
        return totalAPs;
    }

    public void setTotalAPs(Long totalAPs) {
        this.totalAPs = totalAPs;
    }

    public Long getStandByAPCount() {
        return standByAPCount;
    }

    public void setStandByAPCount(Long standByAPCount) {
        this.standByAPCount = standByAPCount;
    }

    public Long getOfflineAPCount() {
        return offlineAPCount;
    }

    public void setOfflineAPCount(Long offlineAPCount) {
        this.offlineAPCount = offlineAPCount;
    }

    public Long getOtherAPCount() {
        return otherAPCount;
    }

    public void setOtherAPCount(Long otherAPCount) {
        this.otherAPCount = otherAPCount;
    }

    public Long getPowerPlantId() {
        return powerPlantId;
    }

    public void setPowerPlantId(Long powerPlantId) {
        this.powerPlantId = powerPlantId;
    }

    public String getPowerPlantName() {
        return powerPlantName;
    }

    public void setPowerPlantName(String powerPlantName) {
        this.powerPlantName = powerPlantName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
