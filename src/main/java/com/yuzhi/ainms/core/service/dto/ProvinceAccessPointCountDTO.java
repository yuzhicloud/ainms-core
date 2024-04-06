package com.yuzhi.ainms.core.service.dto;

public class ProvinceAccessPointCountDTO {

    private Long provinceId;
    private Long count;
    private String provinceName;

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public ProvinceAccessPointCountDTO(Long provinceId, String provinceName, Long count) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.count = count;
    }

    public ProvinceAccessPointCountDTO(Long provinceId, Long count) {
        this.provinceId = provinceId;
        this.count = count;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
