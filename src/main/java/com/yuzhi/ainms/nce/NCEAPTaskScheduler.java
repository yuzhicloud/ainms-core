package com.yuzhi.ainms.nce;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class NCEAPTaskScheduler {

    private static final long INITIAL_DELAY = 60 - LocalTime.now().getMinute();

    private final NCEAPService nceapService;

    public NCEAPTaskScheduler(NCEAPService nceapService) {
        this.nceapService = nceapService;
    }


    @Scheduled(initialDelayString = "#{T(java.time.LocalTime).now().until(T(java.time.LocalTime)." +
     "of((T(java.time.LocalTime).now().getHour() + 1) % 24, 0), " +
      "T(java.time.temporal.ChronoUnit).MINUTES) * 60000}", fixedDelay = 3600000)
    public void scheduleTask() {
        nceapService.syncAllAccessPoints();
    }
}
