package com.yuzhi.ainms.snmp;

import com.yuzhi.ainms.core.service.AccessPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.snmp4j.PDU;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class APEventHandler {

    /**
    * 处理PDU,抽取AP的信息，并更新数据库
    */
    private Logger logger = LoggerFactory.getLogger(APEventHandler.class) ;

    @Autowired
    private AccessPointService accessPointService;

    private static final OID FAULT_TRAP_OID = new OID("1.3.6.1.4.1.2011.6.139.13.1.1.1");
    private static final OID NORMAL_TRAP_OID = new OID("1.3.6.1.4.1.2011.6.139.13.1.1.2");
    private ConcurrentHashMap<String, Lock> macLocks = new ConcurrentHashMap<>();

    public void handleTrapEvent(PDU pdu) {
        for (VariableBinding vb : pdu.getVariableBindings()) {
            // 假设所有Trap都包含设备的MAC地址作为第一个VariableBinding
            String apMac = vb.getVariable().toString();
            Lock lock = macLocks.computeIfAbsent(apMac, k -> new ReentrantLock());

            lock.lock();
            try {
                if (vb.getOid().startsWith(FAULT_TRAP_OID)) {
                    // 处理故障Trap
                    updateDeviceStatus(vb,SNMPConstants.AP_STATUS_OFFLINE);
                } else if (vb.getOid().startsWith(NORMAL_TRAP_OID)) {
                    // 处理正常Trap
                    updateDeviceStatus(vb,SNMPConstants.AP_STATUS_ONLINE);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private void updateDeviceStatus(VariableBinding vb, String status) {
        String apMac = vb.getVariable().toString();
        // 调用设备服务更新状态
        accessPointService.updateAPStateByMac(apMac, status);
        logger.debug("Device status updated for MAC: {}, Status: {}", apMac, status);
    }
}
