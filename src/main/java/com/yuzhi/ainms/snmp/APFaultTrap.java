package com.yuzhi.ainms.snmp;

public class APFaultTrap {

    private String apMac;
    private String apActualType;
    private String apName;
    private int apFaultTimes;
    private Long apId;

    public APFaultTrap(String apMac, String apActualType, String apName, int apFaultTimes, Long apId) {
        this.apMac = apMac;
        this.apActualType = apActualType;
        this.apName = apName;
        this.apFaultTimes = apFaultTimes;
        this.apId = apId;
    }

    public String getApMac() {
        return apMac;
    }

    public void setApMac(String apMac) {
        this.apMac = apMac;
    }

    public String getApActualType() {
        return apActualType;
    }

    public void setApActualType(String apActualType) {
        this.apActualType = apActualType;
    }

    public String getApName() {
        return apName;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    public int getApFaultTimes() {
        return apFaultTimes;
    }

    public void setApFaultTimes(int apFaultTimes) {
        this.apFaultTimes = apFaultTimes;
    }

    public Long getApId() {
        return apId;
    }

    public void setApId(Long apId) {
        this.apId = apId;
    }

    @Override
    public String toString() {
        return "APFaultTrap{" +
            "apMac='" + apMac + '\'' +
            ", apActualType='" + apActualType + '\'' +
            ", apName='" + apName + '\'' +
            ", apFaultTimes='" + apFaultTimes + '\'' +
            ", apId='" + apId + '\'' +
            '}';
    }
}
