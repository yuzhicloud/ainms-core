package com.yuzhi.ainms.nce;

import com.yuzhi.ainms.core.service.AccessPointService;
import com.yuzhi.ainms.core.service.dto.NCEAccessPointDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class NCEAPService {

    private final Logger log = LoggerFactory.getLogger(NCEAPService.class);

    private NCEConfiguration nceConfiguration;
    private final WebClient webClient;
    private final AccessPointService accessPointService;

    public NCEAPService(WebClient webClient, NCEConfiguration nceConfig, AccessPointService accessPointService) {
        this.webClient = webClient;
        this.nceConfiguration = nceConfig;
        this.accessPointService = accessPointService;
    }

    // 每2小时执行一次的任务，获取所有分页的设备数据
    @Scheduled(fixedRate = 7200000)
    public void syncAllAccessPoints() {
        log.debug("==Method call: syncAllAccessPoints");
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
        log.debug("===fetchAllAccessPoints");
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
        log.debug("fetchAccessPointsPage: pageIndex={}", pageIndex);
        return getToken()
            .flatMap(token -> webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .scheme("https")
                    .host(nceConfiguration.getNceServer())
                    .path("/controller/campus/v3/devices")
                    .queryParam("pageIndex", pageIndex)
                    .queryParam("pageSize", 1000)  // 指定每页1000条记录
                    .build())
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(NCEDeviceResponse.class)
                .doOnSuccess(response -> log.debug("Successfully fetched page: {}", pageIndex))
                .doOnError(error -> log.error("Failed to fetch page: {}, error: {}", pageIndex, error.getMessage()))
            )
            .doOnError(error -> log.error("Error in fetchAccessPointsPage: {}", error.getMessage()));
    }

    // 将NCEDeviceResponse.AccessPointData转换为AccessPoint
    private NCEAccessPointDTO convertToAccessPoint(NCEDeviceResponse.AccessPointData data) {
        // 转换逻辑
        NCEAccessPointDTO nceAccessPointDTO = new NCEAccessPointDTO(data.getId(),
        data.getName(),data.getEsn(),data.getNeType(),Integer.getInteger(data.getStatus()));
        return nceAccessPointDTO;
    }

    public Mono<String> getToken() {
        log.debug("===NCEAPService: Getting token");
        String nceUri = "https://" + nceConfiguration.getNceServer() + ":" + nceConfiguration.getNcePort() + "/controller/v2/tokens";
        log.debug("===NCEAPService: nceUri={}", nceUri);
        return webClient.post()
            .uri(nceUri)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new TokenRequest(nceConfiguration.getNceUsername(), nceConfiguration.getNceUserpwd()))
            .retrieve()
            .onStatus(status -> status.isError(), response -> {
                return response.bodyToMono(String.class).flatMap(body -> {
                    log.error("Error with response: Status={}, Body={}", response.statusCode(), body);
                    return Mono.error(new RuntimeException("Error from server: " + body));
                });
            })
            .bodyToMono(NCETokenResponse.class)
            .map(response -> {
                if ("0".equals(response .getErrcode())) {
                    return response.getData().getTokenId(); // 确保您的NCETokenResponse类有getData()和getTokenId()
                } else {
                    log.error("Failed to get token: {}", response.getErrmsg());
                    throw new RuntimeException("Failed to get token: " + response.getErrmsg());
                }
            })
            .doOnSuccess(token -> log.debug("Token retrieved successfully: {}", token))
            .doOnError(error -> log.error("Error retrieving token: {}", error.getMessage()));
    }

    // 用于映射POST请求体的内部类
    static class TokenRequest {
        private String userName;
        private String password;

        public TokenRequest(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }
    }

}
