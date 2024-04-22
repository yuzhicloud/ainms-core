package com.yuzhi.ainms.core.service.stistics;

import com.yuzhi.ainms.core.service.AccessPointService;
import com.yuzhi.ainms.nce.NCEAPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class APStatisticsTask {

    private final Logger log = LoggerFactory.getLogger(APStatisticsTask.class);

    private static final long INITIAL_DELAY = 60 - LocalTime.now().getMinute();

    private final NCEAPService nceapService;

    private final AccessPointService accessPointService;

    public APStatisticsTask(NCEAPService nceapService, AccessPointService accessPointService) {
        this.nceapService = nceapService;
        this.accessPointService = accessPointService;
    }


    @Scheduled(initialDelayString = "#{T(java.time.LocalTime).now().until(T(java.time.LocalTime)." +
     "of((T(java.time.LocalTime).now().getHour() + 1) % 24, 0), " +
      "T(java.time.temporal.ChronoUnit).MINUTES) * 60000}", fixedDelay = 3600000)
    public void scheduleTask() {
        log.info("APStatisticsTask.scheduleTask() called");
//        nceapService.syncAllAccessPoints();
//        accessPointService.updateAPStatisticsByProvince();
//        accessPointService.updateAPStatisticsByPowerPlant();
    }
}
