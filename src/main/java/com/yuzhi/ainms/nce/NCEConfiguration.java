package com.yuzhi.ainms.nce;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nce")
public class NCEConfiguration {

    @Value("${nce.server}")
    private String nceServer;

    @Value("${nce.port}")
    private String ncePort;


    @Value("${nce.nceusername}")
    private String nceUsername;

    @Value("${nce.nceuserpwd}")
    private String nceUserpwd;

    public String getNceServer() {
        return nceServer;
    }

    public void setNceServer(String nceServer) {
        this.nceServer = nceServer;
    }

    public String getNceUsername() {
        return nceUsername;
    }

    public void setNceUsername(String nceUsername) {
        this.nceUsername = nceUsername;
    }

    public String getNceUserpwd() {
        return nceUserpwd;
    }

    public void setNceUserpwd(String nceUserpwd) {
        this.nceUserpwd = nceUserpwd;
    }

    public String getNcePort() {
        return ncePort;
    }

    public void setNcePort(String ncePort) {
        this.ncePort = ncePort;
    }

    @Override
    public String toString() {
        return "NCEConfiguration{" +
            "nceServer='" + nceServer + '\'' +
            ", ncePort='" + ncePort + '\'' +
            ", nceUsername='" + nceUsername + '\'' +
            ", nceUserpwd='" + nceUserpwd + '\'' +
            '}';
    }
}

