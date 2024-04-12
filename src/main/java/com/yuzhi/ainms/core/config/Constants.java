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
    * �豸״̬��'0'---������'1'---�澯��'3'---���ߡ�'4'---δע�ᡣiMaster NCE-WAN�����£�AR�豸�޸澯״̬��*/
    public static final int NCE_AP_STATUS_ACTIVE = 0;
    public static final int NCE_AP_STATUS_OFFLINE= 1;
    private Constants() {}
}
