package com.yuzhi.ainms.nce;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class NCEDeviceResponse {

    private String errcode;
    private String errmsg;
    private Integer pageIndex;
    private Integer pageSize;
    private Integer totalRecords;
    private List<AccessPointData> data;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<AccessPointData> getData() {
        return data;
    }

    public void setData(List<AccessPointData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NCEDeviceResponse{" +
            "errcode='" + errcode + '\'' +
            ", errmsg='" + errmsg + '\'' +
            ", pageIndex=" + pageIndex +
            ", pageSize=" + pageSize +
            ", totalRecords=" + totalRecords +
            ", data=" + data +
            '}';
    }
// 构造器、getter和setter省略

    public static class AccessPointData {
        @JsonProperty("id")
        private String id;
        private String name;
        private String esn;
        private String deviceModel;
        private String deviceType;
        private String status;
        private String siteId;
        private String mac;
        private String ip;
        private String neType;
        private String version;
        private String vendor;
        private String description;
        private String resourceId;
        private String tenantId;
        private String tenantName;
        private String siteName;
        private String createTime;
        private String registerTime;
        private String modifyTime;
        private String startupTime;
        private List<String> tags;
        private String systemIp;
        private String patchVersion;
        private Boolean ztpConfirm;
        private String manageStatus;
        private List<String> manageStatusDownReason;
        private List<String> role;
        private Integer performance;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEsn() {
            return esn;
        }

        public void setEsn(String esn) {
            this.esn = esn;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getNeType() {
            return neType;
        }

        public void setNeType(String neType) {
            this.neType = neType;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVendor() {
            return vendor;
        }

        public void setVendor(String vendor) {
            this.vendor = vendor;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public String getTenantName() {
            return tenantName;
        }

        public void setTenantName(String tenantName) {
            this.tenantName = tenantName;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(String registerTime) {
            this.registerTime = registerTime;
        }

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getStartupTime() {
            return startupTime;
        }

        public void setStartupTime(String startupTime) {
            this.startupTime = startupTime;
        }

        public List<String> getTags() {
            return tags;
        }

        public void setTags(List<String> tags) {
            this.tags = tags;
        }

        public String getSystemIp() {
            return systemIp;
        }

        public void setSystemIp(String systemIp) {
            this.systemIp = systemIp;
        }

        public String getPatchVersion() {
            return patchVersion;
        }

        public void setPatchVersion(String patchVersion) {
            this.patchVersion = patchVersion;
        }

        public Boolean getZtpConfirm() {
            return ztpConfirm;
        }

        public void setZtpConfirm(Boolean ztpConfirm) {
            this.ztpConfirm = ztpConfirm;
        }

        public String getManageStatus() {
            return manageStatus;
        }

        public void setManageStatus(String manageStatus) {
            this.manageStatus = manageStatus;
        }

        public List<String> getManageStatusDownReason() {
            return manageStatusDownReason;
        }

        public void setManageStatusDownReason(List<String> manageStatusDownReason) {
            this.manageStatusDownReason = manageStatusDownReason;
        }

        public List<String> getRole() {
            return role;
        }

        public void setRole(List<String> role) {
            this.role = role;
        }

        public Integer getPerformance() {
            return performance;
        }

        public void setPerformance(Integer performance) {
            this.performance = performance;
        }
    }

}
