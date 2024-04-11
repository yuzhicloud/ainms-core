package com.yuzhi.ainms.nce;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import javax.net.ssl.SSLException;

@Configuration
public class NCERestClientConfig {

    @Bean
    public WebClient webClient() throws SSLException {
        SslContextBuilder sslContextBuilder = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE); // 信任所有证书，不推荐用于生产

        HttpClient httpClient = HttpClient.create()
            .secure(sslContextSpec -> sslContextSpec.sslContext(sslContextBuilder));

        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }
}

