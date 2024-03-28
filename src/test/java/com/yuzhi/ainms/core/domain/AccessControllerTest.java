package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.AccessControllerTestSamples.*;
import static com.yuzhi.ainms.core.domain.AccessPointGroupTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AccessControllerTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AccessController.class);
    AccessController accessController1 = getAccessControllerSample1();
    AccessController accessController2 = new AccessController();
    assertThat(accessController1).isNotEqualTo(accessController2);

    accessController2.setId(accessController1.getId());
    assertThat(accessController1).isEqualTo(accessController2);

    accessController2 = getAccessControllerSample2();
    assertThat(accessController1).isNotEqualTo(accessController2);
  }

  @Test
  void accessPointGroupTest() throws Exception {
    AccessController accessController =
      getAccessControllerRandomSampleGenerator();
    AccessPointGroup accessPointGroupBack =
      getAccessPointGroupRandomSampleGenerator();

    accessController.addAccessPointGroup(accessPointGroupBack);
    assertThat(accessController.getAccessPointGroups()).containsOnly(
      accessPointGroupBack
    );
    assertThat(accessPointGroupBack.getController()).isEqualTo(
      accessController
    );

    accessController.removeAccessPointGroup(accessPointGroupBack);
    assertThat(accessController.getAccessPointGroups()).doesNotContain(
      accessPointGroupBack
    );
    assertThat(accessPointGroupBack.getController()).isNull();

    accessController.accessPointGroups(
      new HashSet<>(Set.of(accessPointGroupBack))
    );
    assertThat(accessController.getAccessPointGroups()).containsOnly(
      accessPointGroupBack
    );
    assertThat(accessPointGroupBack.getController()).isEqualTo(
      accessController
    );

    accessController.setAccessPointGroups(new HashSet<>());
    assertThat(accessController.getAccessPointGroups()).doesNotContain(
      accessPointGroupBack
    );
    assertThat(accessPointGroupBack.getController()).isNull();
  }
}
