package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.AccessPointGroupTestSamples.*;
import static com.yuzhi.ainms.core.domain.AccessPointTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccessPointTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AccessPoint.class);
    AccessPoint accessPoint1 = getAccessPointSample1();
    AccessPoint accessPoint2 = new AccessPoint();
    assertThat(accessPoint1).isNotEqualTo(accessPoint2);

    accessPoint2.setId(accessPoint1.getId());
    assertThat(accessPoint1).isEqualTo(accessPoint2);

    accessPoint2 = getAccessPointSample2();
    assertThat(accessPoint1).isNotEqualTo(accessPoint2);
  }

  @Test
  void groupTest() throws Exception {
    AccessPoint accessPoint = getAccessPointRandomSampleGenerator();
    AccessPointGroup accessPointGroupBack =
      getAccessPointGroupRandomSampleGenerator();

    accessPoint.setGroup(accessPointGroupBack);
    assertThat(accessPoint.getGroup()).isEqualTo(accessPointGroupBack);

    accessPoint.group(null);
    assertThat(accessPoint.getGroup()).isNull();
  }
}
