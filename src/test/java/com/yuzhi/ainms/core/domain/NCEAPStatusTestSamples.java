package com.yuzhi.ainms.core.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class NCEAPStatusTestSamples {

  private static final Random random = new Random();
  private static final AtomicInteger intCount = new AtomicInteger(
    random.nextInt() + (2 * Short.MAX_VALUE)
  );

  public static NCEAPStatus getNCEAPStatusSample1() {
    return new NCEAPStatus().id(1).apSn("apSn1").apStatus(1);
  }

  public static NCEAPStatus getNCEAPStatusSample2() {
    return new NCEAPStatus().id(2).apSn("apSn2").apStatus(2);
  }

  public static NCEAPStatus getNCEAPStatusRandomSampleGenerator() {
    return new NCEAPStatus()
      .id(intCount.incrementAndGet())
      .apSn(UUID.randomUUID().toString())
      .apStatus(intCount.incrementAndGet());
  }
}
