package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.CountryStisticsTestSamples.*;
import static com.yuzhi.ainms.core.domain.ProvinceStisticsTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryStisticsTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CountryStistics.class);
    CountryStistics countryStistics1 = getCountryStisticsSample1();
    CountryStistics countryStistics2 = new CountryStistics();
    assertThat(countryStistics1).isNotEqualTo(countryStistics2);

    countryStistics2.setId(countryStistics1.getId());
    assertThat(countryStistics1).isEqualTo(countryStistics2);

    countryStistics2 = getCountryStisticsSample2();
    assertThat(countryStistics1).isNotEqualTo(countryStistics2);
  }

  @Test
  void provinceTest() throws Exception {
    CountryStistics countryStistics = getCountryStisticsRandomSampleGenerator();
    ProvinceStistics provinceStisticsBack =
      getProvinceStisticsRandomSampleGenerator();

    countryStistics.addProvince(provinceStisticsBack);
    assertThat(countryStistics.getProvinces()).containsOnly(
      provinceStisticsBack
    );
    assertThat(provinceStisticsBack.getCountry()).isEqualTo(countryStistics);

    countryStistics.removeProvince(provinceStisticsBack);
    assertThat(countryStistics.getProvinces()).doesNotContain(
      provinceStisticsBack
    );
    assertThat(provinceStisticsBack.getCountry()).isNull();

    countryStistics.provinces(new HashSet<>(Set.of(provinceStisticsBack)));
    assertThat(countryStistics.getProvinces()).containsOnly(
      provinceStisticsBack
    );
    assertThat(provinceStisticsBack.getCountry()).isEqualTo(countryStistics);

    countryStistics.setProvinces(new HashSet<>());
    assertThat(countryStistics.getProvinces()).doesNotContain(
      provinceStisticsBack
    );
    assertThat(provinceStisticsBack.getCountry()).isNull();
  }
}
