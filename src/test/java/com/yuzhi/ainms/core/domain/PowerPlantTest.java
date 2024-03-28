package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.AccessPointGroupTestSamples.*;
import static com.yuzhi.ainms.core.domain.PowerPlantTestSamples.*;
import static com.yuzhi.ainms.core.domain.ProvinceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PowerPlantTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PowerPlant.class);
    PowerPlant powerPlant1 = getPowerPlantSample1();
    PowerPlant powerPlant2 = new PowerPlant();
    assertThat(powerPlant1).isNotEqualTo(powerPlant2);

    powerPlant2.setId(powerPlant1.getId());
    assertThat(powerPlant1).isEqualTo(powerPlant2);

    powerPlant2 = getPowerPlantSample2();
    assertThat(powerPlant1).isNotEqualTo(powerPlant2);
  }

  @Test
  void accessPointGroupTest() throws Exception {
    PowerPlant powerPlant = getPowerPlantRandomSampleGenerator();
    AccessPointGroup accessPointGroupBack =
      getAccessPointGroupRandomSampleGenerator();

    powerPlant.addAccessPointGroup(accessPointGroupBack);
    assertThat(powerPlant.getAccessPointGroups()).containsOnly(
      accessPointGroupBack
    );
    assertThat(accessPointGroupBack.getPowerPlant()).isEqualTo(powerPlant);

    powerPlant.removeAccessPointGroup(accessPointGroupBack);
    assertThat(powerPlant.getAccessPointGroups()).doesNotContain(
      accessPointGroupBack
    );
    assertThat(accessPointGroupBack.getPowerPlant()).isNull();

    powerPlant.accessPointGroups(new HashSet<>(Set.of(accessPointGroupBack)));
    assertThat(powerPlant.getAccessPointGroups()).containsOnly(
      accessPointGroupBack
    );
    assertThat(accessPointGroupBack.getPowerPlant()).isEqualTo(powerPlant);

    powerPlant.setAccessPointGroups(new HashSet<>());
    assertThat(powerPlant.getAccessPointGroups()).doesNotContain(
      accessPointGroupBack
    );
    assertThat(accessPointGroupBack.getPowerPlant()).isNull();
  }

  @Test
  void provinceTest() throws Exception {
    PowerPlant powerPlant = getPowerPlantRandomSampleGenerator();
    Province provinceBack = getProvinceRandomSampleGenerator();

    powerPlant.setProvince(provinceBack);
    assertThat(powerPlant.getProvince()).isEqualTo(provinceBack);

    powerPlant.province(null);
    assertThat(powerPlant.getProvince()).isNull();
  }
}
