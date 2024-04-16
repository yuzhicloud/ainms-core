package com.yuzhi.ainms.nce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuzhi.ainms.core.service.AccessPointService;
import com.yuzhi.ainms.core.service.dto.NCEAccessPointDTO;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.tcp.SslProvider;
import java.time.Duration;

@Service
@Transactional
public class NCEAPService {

    private final Logger log = LoggerFactory.getLogger(NCEAPService.class);

    private final NCEConfiguration nceConfiguration;
    private final WebClient webClient;
    private final AccessPointService accessPointService;

    public NCEAPService(WebClient webClient, NCEConfiguration nceConfig, AccessPointService accessPointService) {

        this.nceConfiguration = nceConfig;
        this.accessPointService = accessPointService;
        HttpClient httpClient = HttpClient.create(ConnectionProvider.builder("custom")
                .maxConnections(500) // 设置最大连接数
                .pendingAcquireMaxCount(-1) // 无限制等待队列
                .maxIdleTime(Duration.ofMinutes(30)) // 设置连接最大空闲时间
                .maxLifeTime(Duration.ofHours(1)) // 设置连接最大生存时间
                .build())
            .secure(sslContextSpec -> sslContextSpec.sslContext(SslProvider.builder()
                .sslContext(SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE))
                .defaultConfiguration(SslProvider.DefaultConfigurationType.NONE)
                .build().getSslContext()))
            .responseTimeout(Duration.ofSeconds(30))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(30)) // 读取超时
                    .addHandlerLast(new WriteTimeoutHandler(30))); // 写入超时

        this.webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .baseUrl("https://10.170.69.87:18002")
            .build();
    }

    // 每2小时执行一次的任务，获取所有分页的设备数据,由NCEAPTaskScheduler调用
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
            .doOnNext(response -> {
                // 此处记录每页数据量
                log.debug("Fetched page with index: {}, size: {}", response.getPageIndex(), response.getData().size());
            })
            .flatMapIterable(NCEDeviceResponse::getData)
            .map(this::convertToAccessPoint)
            .doOnNext(accessPoint -> {
                // 这里可以记录转换后每个access point的信息，或简单记录条数
                log.debug("Processed access point: {}", accessPoint.getApSn());
            });
    }

    private Mono<NCEDeviceResponse> fetchAccessPointsPage(int pageIndex) {
        log.debug("Start fetching devices from page: pageIndex={}, pageSize={}", pageIndex, 20);

        return getToken()
            .flatMap(token -> {
                String requestUri = UriComponentsBuilder.fromUriString("https://" + nceConfiguration.getNceServer() + ":" + nceConfiguration.getNcePort())
                    .path("/controller/campus/v3/devices")
                    .queryParam("pageIndex", pageIndex)
                    .queryParam("pageSize", 100)  // 指定每页1000条记录
                    .queryParam("deviceType","AP")
                    .toUriString();

                log.debug("fetchAccessPointsPage::Requesting URI: {}", requestUri);

                return webClient.get()
                    .uri(requestUri)
                    .header("X-AUTH-TOKEN", token)  // Use the token as received from getToken() if it's directly applicable
                    .header("Accept-Language", "en-US")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .onStatus(status -> status.isError(), response -> {
                        return response.bodyToMono(String.class).flatMap(body -> {
                            log.error("Error with response: Status={}, Body={}", response.statusCode(), body);
                            return Mono.error(new RuntimeException("Error from server: " + body));
                        });
                    })
                    .bodyToMono(String.class)
                    .doOnNext(jsonString -> log.debug("FetchAP:Raw JSON response: {}", jsonString))
                    .flatMap(jsonString -> {
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            NCEDeviceResponse response = objectMapper.readValue(jsonString, NCEDeviceResponse.class);
                            log.debug("Successfully fetched page: {}, Total devices: {}", pageIndex, response.getTotalRecords());
                            return Mono.just(response);
                        } catch (JsonProcessingException e) {
                            log.error("JSON parsing error: {}", e.getMessage());
                            return Mono.error(e);
                        }
                    });
            })
            .doOnError(error -> log.error("Error in getToken or fetchAccessPointsPage: {}", error.getMessage()));
    }

    // 将NCEDeviceResponse.AccessPointData转换为AccessPoint
    private NCEAccessPointDTO convertToAccessPoint(NCEDeviceResponse.AccessPointData data) {
        // 转换逻辑
        NCEAccessPointDTO nceAccessPointDTO = new NCEAccessPointDTO(data.getId(),
            data.getName(), data.getEsn(), data.getNeType(), Integer.parseInt(data.getStatus()));
        return nceAccessPointDTO;
    }

    public Mono<String> getToken() {
        log.debug("Getting token from NCE");
        String nceUri = "https://" + nceConfiguration.getNceServer() + ":" + nceConfiguration.getNcePort() + "/controller/v2/tokens";
        log.debug("NCE URI for token: {}", nceUri);
        log.debug("NCE Username: {}", nceConfiguration.getNceUsername());
        log.debug("NCE Password: {}", nceConfiguration.getNceUserpwd());

        return webClient.post()
            .uri(nceUri)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.valueOf("application/json;charset=UTF-8"))
            .bodyValue(new NCETokenRequest(nceConfiguration.getNceUsername(), nceConfiguration.getNceUserpwd()))
            .retrieve()
            .onStatus(status -> status.isError(), response -> {
                return response.bodyToMono(String.class).flatMap(body -> {
                    log.error("Error with response: Status={}, Body={}", response.statusCode(), body);
                    return Mono.error(new RuntimeException("Error from server: " + body));
                });
            })
            .bodyToMono(String.class)
            .doOnNext(jsonString -> log.debug("Raw JSON response: {}", jsonString))
            .flatMap(jsonString -> {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    NCETokenResponse response = objectMapper.readValue(jsonString, NCETokenResponse.class);
                    if (response == null || response.getData() == null || response.getData().getTokenId() == null) {
                        log.error("Token data is missing or token ID is null");
                        return Mono.error(new IllegalStateException("Token data is missing or token ID is null"));
                    }
                    if ("0".equals(response.getErrcode())) {
                        return Mono.just(response.getData().getTokenId());
                    } else {
                        String errorMessage = String.format("Failed to get token: %s", response.getErrmsg());
                        log.error(errorMessage);
                        return Mono.error(new RuntimeException(errorMessage));
                    }
                } catch (JsonProcessingException e) {
                    log.error("JSON parsing error: {}", e.getMessage());
                    return Mono.error(e); // Properly handling JSON parsing errors.
                }
            });
    }
}
