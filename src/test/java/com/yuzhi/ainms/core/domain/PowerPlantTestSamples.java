package com.yuzhi.ainms.core.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PowerPlantTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PowerPlant getPowerPlantSample1() {
        return new PowerPlant().id(1L).powerPlantName("powerPlantName1");
    }

    public static PowerPlant getPowerPlantSample2() {
        return new PowerPlant().id(2L).powerPlantName("powerPlantName2");
    }

    public static PowerPlant getPowerPlantRandomSampleGenerator() {
        return new PowerPlant().id(longCount.incrementAndGet()).powerPlantName(UUID.randomUUID().toString());
    }
}
