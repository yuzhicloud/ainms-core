package com.yuzhi.ainms.nce;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "nce")
public class NCEConfiguration {

    private String nceServer;
    private String nceUsername;
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

    @Override
    public String toString() {
        return "NCEConfiguration{" +
            "nceServer='" + nceServer + '\'' +
            ", nceUsername='" + nceUsername + '\'' +
            ", nceUserpwd='" + nceUserpwd + '\'' +
            '}';
    }
}

