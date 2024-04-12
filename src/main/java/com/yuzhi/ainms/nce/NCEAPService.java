package com.yuzhi.ainms.nce;

import com.yuzhi.ainms.core.service.AccessPointService;
import com.yuzhi.ainms.core.service.dto.NCEAccessPointDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NCEAPService {

    private final Logger log = LoggerFactory.getLogger(NCEAPService.class);

    private NCEConfiguration nceConfiguration;
    private final WebClient webClient;
    private final AccessPointService accessPointService;

    @Autowired
    public NCEAPService(WebClient webClient, NCEConfiguration nceConfig, AccessPointService accessPointService) {
        this.webClient = webClient;
        this.nceConfiguration = nceConfig;
        this.accessPointService = accessPointService;
    }

    private String buildRequestUri(int pageIndex) {
        return "https://" + nceConfiguration.getNceServer() + ":18002/controller/campus/v3/devices?pageIndex=" + pageIndex;
    }

    // 每2小时执行一次的任务，获取所有分页的设备数据
    @Scheduled(fixedRate = 7200000)
    public void syncAllAccessPoints() {
        // Now you have a List<NCEAccessPointDTO>, you can process it
        fetchAllAccessPoints()
            .collectList() // Collect all NCEAccessPointDTO objects into a List
            .subscribe(accessPointService::updateAccessPoints, error -> {
                // Error handling logic
                log.error("==syncAllAccessPoints Error during synchronization: {}", error.getMessage());
            }, () -> {
                // Completion logic
                log.debug("== syncAllAccessPoints::Synchronization completed.");
            });
    }

    // 获取所有分页的设备数据
    private Flux<NCEAccessPointDTO> fetchAllAccessPoints() {
        return fetchAccessPointsPage(1)
            .expand(response -> {
                if (response.getPageIndex() < response.getTotalRecords() / response.getPageSize() + 1) {
                    return fetchAccessPointsPage(response.getPageIndex() + 1);
                }
                return Mono.empty();
            })
            .flatMapIterable(NCEDeviceResponse::getData)
            .map(this::convertToAccessPoint);
    }

    // 获取指定页码的设备数据
    private Mono<NCEDeviceResponse> fetchAccessPointsPage(int pageIndex) {
        return getToken().flatMap(token ->
            webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host(nceConfiguration.getNceServer())
                    .path("/controller/campus/v3/devices")
                    .queryParam("pageIndex", pageIndex)
                    .queryParam("pageSize", 1000) // 指定每页1000条记录
                    .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(NCEDeviceResponse.class));
    }

    // 将NCEDeviceResponse.AccessPointData转换为AccessPoint
    private NCEAccessPointDTO convertToAccessPoint(NCEDeviceResponse.AccessPointData data) {
        // 转换逻辑
        NCEAccessPointDTO nceAccessPointDTO = new NCEAccessPointDTO(data.getId(),
        data.getName(),data.getEsn(),data.getNeType(),Integer.getInteger(data.getStatus()));
        return nceAccessPointDTO;
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
