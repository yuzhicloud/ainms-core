package com.yuzhi.ainms.core.service.dto;

public class ProvinceAPStatisticsDTO {

    private Long totalAPs;
    private Long standByAPCount;
    private Long offlineAPCount;
    private Long otherAPCount;
    private Long provinceId;
    private String provinceName;

    public ProvinceAPStatisticsDTO(String provinceName, Long totalAPs, Long standByAPCount, Long offlineAPCount, Long otherAPCount) {
        this.provinceName = provinceName;
        this.totalAPs = totalAPs;
        this.standByAPCount = standByAPCount;
        this.offlineAPCount = offlineAPCount;
        this.otherAPCount = otherAPCount;
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

}
