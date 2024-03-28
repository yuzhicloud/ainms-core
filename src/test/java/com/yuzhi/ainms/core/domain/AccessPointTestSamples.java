package com.yuzhi.ainms.core.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AccessPointTestSamples {

  private static final Random random = new Random();
  private static final AtomicLong longCount = new AtomicLong(
    random.nextInt() + (2 * Integer.MAX_VALUE)
  );
  private static final AtomicInteger intCount = new AtomicInteger(
    random.nextInt() + (2 * Short.MAX_VALUE)
  );

  public static AccessPoint getAccessPointSample1() {
    return new AccessPoint()
      .id(1L)
      .nedn("nedn1")
      .neid(1)
      .aliasname("aliasname1")
      .nename("nename1")
      .necategory("necategory1")
      .netype("netype1")
      .nevendorname("nevendorname1")
      .neesn("neesn1")
      .neip("neip1")
      .nemac("nemac1")
      .version("version1")
      .nestate(1)
      .createtime("createtime1")
      .neiptype(1)
      .subnet("subnet1")
      .neosversion("neosversion1");
  }

  public static AccessPoint getAccessPointSample2() {
    return new AccessPoint()
      .id(2L)
      .nedn("nedn2")
      .neid(2)
      .aliasname("aliasname2")
      .nename("nename2")
      .necategory("necategory2")
      .netype("netype2")
      .nevendorname("nevendorname2")
      .neesn("neesn2")
      .neip("neip2")
      .nemac("nemac2")
      .version("version2")
      .nestate(2)
      .createtime("createtime2")
      .neiptype(2)
      .subnet("subnet2")
      .neosversion("neosversion2");
  }

  public static AccessPoint getAccessPointRandomSampleGenerator() {
    return new AccessPoint()
      .id(longCount.incrementAndGet())
      .nedn(UUID.randomUUID().toString())
      .neid(intCount.incrementAndGet())
      .aliasname(UUID.randomUUID().toString())
      .nename(UUID.randomUUID().toString())
      .necategory(UUID.randomUUID().toString())
      .netype(UUID.randomUUID().toString())
      .nevendorname(UUID.randomUUID().toString())
      .neesn(UUID.randomUUID().toString())
      .neip(UUID.randomUUID().toString())
      .nemac(UUID.randomUUID().toString())
      .version(UUID.randomUUID().toString())
      .nestate(intCount.incrementAndGet())
      .createtime(UUID.randomUUID().toString())
      .neiptype(intCount.incrementAndGet())
      .subnet(UUID.randomUUID().toString())
      .neosversion(UUID.randomUUID().toString());
  }
}
