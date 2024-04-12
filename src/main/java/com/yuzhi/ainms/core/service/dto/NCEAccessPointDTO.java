package com.yuzhi.ainms.core.service.dto;

public class NCEAccessPointDTO {

    private String id;
    private String apName;
    private String apSn;
    private String apNEType;
    private int apStatus;

    public NCEAccessPointDTO(String id, String apName, String apSn, String apNEType, int apStatus) {
        this.id = id;
        this.apName = apName;
        this.apSn = apSn;
        this.apNEType = apNEType;
        this.apStatus = apStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApName() {
        return apName;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    public String getApSn() {
        return apSn;
    }

    public void setApSn(String apSn) {
        this.apSn = apSn;
    }

    public String getApNEType() {
        return apNEType;
    }

    public void setApNEType(String apNEType) {
        this.apNEType = apNEType;
    }

    public int getApStatus() {
        return apStatus;
    }

    public void setApStatus(int apStatus) {
        this.apStatus = apStatus;
    }

    @Override
    public String toString() {
        return "NCEAccessPointDTO{" +
            "id='" + id + '\'' +
            ", apName='" + apName + '\'' +
            ", apSn='" + apSn + '\'' +
            ", apNEType='" + apNEType + '\'' +
            ", apStatus=" + apStatus +
            '}';
    }
    /**
    private String name;
    private String description;
    private String type;
    private String status;
    private String location;
    private String ipAddress;
    private String macAddress;
    private String serialNumber;
    private String model;
    private String firmwareVersion;
    private String softwareVersion;
    private String hardwareVersion;
    private String manufacturer;
    private String created;
    private String updated;
    private String createdBy;
    private String updatedBy;
    private String siteId;
    private String siteName;
    private String siteDescription;
    private String siteLocation;
    private String siteType;
    private String siteStatus;
    private String siteCreated;
    private String siteUpdated;
    private String siteCreatedBy;
    private String siteUpdatedBy;

     **/

}
