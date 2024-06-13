package com.yuzhi.ainms.core.config;

/**
 * Application constants.
 */
public final class Constants {

    public static final String SYSTEM = "system";
    public static final String DEFAULT_LANGUAGE = "en";

    /* NMS AP Status
    * INTEGER{idle(1),autofind(2),typeNotMatch(3),fault(4),config(5),configFailed(6),download(7),
    * normal(8),committing(9),commitFailed(10),standby(11),verMismatch(12),nameConflicted(13),
    * invalid(14),countryCodeMismatch(15)}
     */
    public static final int NMS_AP_STATUS_ACTIVE = 11;
    public static final int NMS_AP_STATUS_OFFLINE= 4;

    /* NCE AP Status
     * 设备状态，'0'---正常、'1'---告警、'3'---离线、'4'---未注册。iMaster NCE-WAN场景下，AR设备无告警状态。*/
    public static final int NCE_AP_STATUS_ACTIVE = 0;
    public static final int NCE_AP_STATUS_FAULT = 1;
    public static final int NCE_AP_STATUS_OFFLINE= 3;
    public static final int NCE_AP_STATUS_UNREGISTER= 4;
    private Constants() {}
}
