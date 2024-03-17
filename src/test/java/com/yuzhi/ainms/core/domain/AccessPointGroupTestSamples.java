package com.yuzhi.ainms.core.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccessPointGroupTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccessPointGroup getAccessPointGroupSample1() {
        return new AccessPointGroup().id(1L).apgId(1L).name("name1");
    }

    public static AccessPointGroup getAccessPointGroupSample2() {
        return new AccessPointGroup().id(2L).apgId(2L).name("name2");
    }

    public static AccessPointGroup getAccessPointGroupRandomSampleGenerator() {
        return new AccessPointGroup().id(longCount.incrementAndGet()).apgId(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
