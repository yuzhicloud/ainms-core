package com.yuzhi.ainms.core.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProvinceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Province getProvinceSample1() {
        return new Province().id(1L).provinceCode(1L).provinceName("provinceName1");
    }

    public static Province getProvinceSample2() {
        return new Province().id(2L).provinceCode(2L).provinceName("provinceName2");
    }

    public static Province getProvinceRandomSampleGenerator() {
        return new Province()
            .id(longCount.incrementAndGet())
            .provinceCode(longCount.incrementAndGet())
            .provinceName(UUID.randomUUID().toString());
    }
}
