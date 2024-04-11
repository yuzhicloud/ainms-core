package com.yuzhi.ainms.nce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class APService {

    @Autowired
    private NCEConfiguration nceConfiguration;
    private final WebClient webClient;

    @Autowired
    public APService(WebClient webClient, NCEConfiguration nceConfig) {
        this.webClient = webClient;
        this.nceConfiguration = nceConfig;
    }

    // 定义每2小时执行一次的任务
    @Scheduled(fixedRate = 7200000)
    public void fetchAccessPoints() {
        // 假设getToken()是获取token的方法，你需要根据你的逻辑实现它
        Mono token = getToken();

        webClient.get()
            .uri("https://" + nceConfiguration.getNceServer() + ":18002/controller/campus/v3/devices")
            .header("Authorization", "Bearer " + token) // 假设使用Bearer Token认证
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(response -> {
                // 这里处理你的业务逻辑
                System.out.println("Response from NCE Server: " + response);
            });
    }

    public Mono<String> getToken() {
        return webClient.post() // 发送POST请求
            .uri("https://" + nceConfiguration.getNceServer() + "/controller/v2/tokens")
            .bodyValue(new TokenRequest(nceConfiguration.getNceUsername(), nceConfiguration.getNceUserpwd()))
            .retrieve() // 提取响应体
            .bodyToMono(NCETokenResponse.class) // 将响应体转换为NCETokenResponse类的实例
            .map(response -> {
                if ("0".equals(response.getErrcode())) {
                    return response.getData().getTokenId(); // 从NCETokenResponse实例中获取Token ID
                } else {
                    throw new RuntimeException("Failed to get token: " + response.getErrmsg());
                }
            });
    }

    // 用于映射POST请求体的内部类
    static class TokenRequest {
        private final String userName;
        private final String password;

        public TokenRequest(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        // Getter方法省略
    }
}
