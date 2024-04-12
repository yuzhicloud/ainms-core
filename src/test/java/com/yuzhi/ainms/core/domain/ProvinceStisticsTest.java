package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.CountryStisticsTestSamples.*;
import static com.yuzhi.ainms.core.domain.PowerPlantStisticsTestSamples.*;
import static com.yuzhi.ainms.core.domain.ProvinceStisticsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProvinceStisticsTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProvinceStistics.class);
    ProvinceStistics provinceStistics1 = getProvinceStisticsSample1();
    ProvinceStistics provinceStistics2 = new ProvinceStistics();
    assertThat(provinceStistics1).isNotEqualTo(provinceStistics2);

    provinceStistics2.setId(provinceStistics1.getId());
    assertThat(provinceStistics1).isEqualTo(provinceStistics2);

    provinceStistics2 = getProvinceStisticsSample2();
    assertThat(provinceStistics1).isNotEqualTo(provinceStistics2);
  }

  @Test
  void stationTest() throws Exception {
    ProvinceStistics provinceStistics =
      getProvinceStisticsRandomSampleGenerator();
    PowerPlantStistics powerPlantStisticsBack =
      getPowerPlantStisticsRandomSampleGenerator();

    provinceStistics.addStation(powerPlantStisticsBack);
    assertThat(provinceStistics.getStations()).containsOnly(
      powerPlantStisticsBack
    );
    assertThat(powerPlantStisticsBack.getProvince()).isEqualTo(
      provinceStistics
    );

    provinceStistics.removeStation(powerPlantStisticsBack);
    assertThat(provinceStistics.getStations()).doesNotContain(
      powerPlantStisticsBack
    );
    assertThat(powerPlantStisticsBack.getProvince()).isNull();

    provinceStistics.stations(new HashSet<>(Set.of(powerPlantStisticsBack)));
    assertThat(provinceStistics.getStations()).containsOnly(
      powerPlantStisticsBack
    );
    assertThat(powerPlantStisticsBack.getProvince()).isEqualTo(
      provinceStistics
    );

    provinceStistics.setStations(new HashSet<>());
    assertThat(provinceStistics.getStations()).doesNotContain(
      powerPlantStisticsBack
    );
    assertThat(powerPlantStisticsBack.getProvince()).isNull();
  }

  @Test
  void countryTest() throws Exception {
    ProvinceStistics provinceStistics =
      getProvinceStisticsRandomSampleGenerator();
    CountryStistics countryStisticsBack =
      getCountryStisticsRandomSampleGenerator();

    provinceStistics.setCountry(countryStisticsBack);
    assertThat(provinceStistics.getCountry()).isEqualTo(countryStisticsBack);

    provinceStistics.country(null);
    assertThat(provinceStistics.getCountry()).isNull();
  }
}
