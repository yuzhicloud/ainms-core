package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.PowerPlantStisticsTestSamples.*;
import static com.yuzhi.ainms.core.domain.ProvinceStisticsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PowerPlantStisticsTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PowerPlantStistics.class);
    PowerPlantStistics powerPlantStistics1 = getPowerPlantStisticsSample1();
    PowerPlantStistics powerPlantStistics2 = new PowerPlantStistics();
    assertThat(powerPlantStistics1).isNotEqualTo(powerPlantStistics2);

    powerPlantStistics2.setId(powerPlantStistics1.getId());
    assertThat(powerPlantStistics1).isEqualTo(powerPlantStistics2);

    powerPlantStistics2 = getPowerPlantStisticsSample2();
    assertThat(powerPlantStistics1).isNotEqualTo(powerPlantStistics2);
  }

  @Test
  void provinceTest() throws Exception {
    PowerPlantStistics powerPlantStistics =
      getPowerPlantStisticsRandomSampleGenerator();
    ProvinceStistics provinceStisticsBack =
      getProvinceStisticsRandomSampleGenerator();

    powerPlantStistics.setProvince(provinceStisticsBack);
    assertThat(powerPlantStistics.getProvince()).isEqualTo(
      provinceStisticsBack
    );

    powerPlantStistics.province(null);
    assertThat(powerPlantStistics.getProvince()).isNull();
  }
}
