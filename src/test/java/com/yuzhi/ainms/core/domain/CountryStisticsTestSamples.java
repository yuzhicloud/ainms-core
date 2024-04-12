package com.yuzhi.ainms.core.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CountryStisticsTestSamples {

  private static final Random random = new Random();
  private static final AtomicLong longCount = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );

  public static CountryStistics getCountryStisticsSample1() {
    return new CountryStistics()
      .id(1L)
      .name("name1")
      .totalCount(1L)
      .onlineCount(1L)
      .offlineCount(1L)
      .otherCount(1L);
  }

  public static CountryStistics getCountryStisticsSample2() {
    return new CountryStistics()
      .id(2L)
      .name("name2")
      .totalCount(2L)
      .onlineCount(2L)
      .offlineCount(2L)
      .otherCount(2L);
  }

  public static CountryStistics getCountryStisticsRandomSampleGenerator() {
    return new CountryStistics()
      .id(longCount.incrementAndGet())
      .name(UUID.randomUUID().toString())
      .totalCount(longCount.incrementAndGet())
      .onlineCount(longCount.incrementAndGet())
      .offlineCount(longCount.incrementAndGet())
      .otherCount(longCount.incrementAndGet());
  }
}
