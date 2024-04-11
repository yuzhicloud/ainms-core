package com.yuzhi.ainms.snmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TrapReceiver implements CommandResponder {
    private Logger log = LoggerFactory.getLogger(TrapReceiver.class);

    private APEventHandler apEventHandler;
    private Snmp snmp = null;

    /*** userInfo **/
    private String snmpUser = "clypgac";
    private String authKey = "longyuandianli@123";
    private String privKey = "longyuandianli@123";

    public TrapReceiver(APEventHandler apEventHandler) {
        this.apEventHandler = apEventHandler;
    }

    public void start() throws IOException {
        MessageDispatcherImpl messageDispatcher = new MessageDispatcherImpl();
        messageDispatcher.addMessageProcessingModel(new MPv3());

        // 加载和配置USM
        USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
        SecurityModels.getInstance().addSecurityModel(usm);

        // 你的用户名称、认证和加密密码应该是与设备上配置的一致
        UsmUser user = new UsmUser(
            new OctetString(snmpUser),
            AuthSHA.ID, new OctetString(authKey),
            PrivAES256.ID, new OctetString(privKey)
        );

        // 添加用户到USM
        usm.addUser(new OctetString(snmpUser), user);

        TransportMapping<?> transport = new DefaultUdpTransportMapping(new UdpAddress("0.0.0.0/162"));
        snmp = new Snmp(messageDispatcher, transport);
        snmp.listen();
        snmp.addCommandResponder(this);

        log.debug("Listening for Traps on port 162");
    }

    @Override
    public void processPdu(CommandResponderEvent event) {
        PDU pdu = event.getPDU();
        if (pdu != null) {
            log.debug("Trap Received from " + event.getPeerAddress() + ": " + pdu.toString());
            apEventHandler.handleTrapEvent(pdu);
        }
    }
}

