package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.AccessPointGroupTestSamples.*;
import static com.yuzhi.ainms.core.domain.PowerPlantTestSamples.*;
import static com.yuzhi.ainms.core.domain.ProvinceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
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
        AccessPointGroup accessPointGroupBack = getAccessPointGroupRandomSampleGenerator();

        powerPlant.setAccessPointGroup(accessPointGroupBack);
        assertThat(powerPlant.getAccessPointGroup()).isEqualTo(accessPointGroupBack);
        assertThat(accessPointGroupBack.getPowerPlant()).isEqualTo(powerPlant);

        powerPlant.accessPointGroup(null);
        assertThat(powerPlant.getAccessPointGroup()).isNull();
        assertThat(accessPointGroupBack.getPowerPlant()).isNull();
    }

    @Test
    void powerPlantTest() throws Exception {
        PowerPlant powerPlant = getPowerPlantRandomSampleGenerator();
        Province provinceBack = getProvinceRandomSampleGenerator();

        powerPlant.setPowerPlant(provinceBack);
        assertThat(powerPlant.getPowerPlant()).isEqualTo(provinceBack);

        powerPlant.powerPlant(null);
        assertThat(powerPlant.getPowerPlant()).isNull();
    }
}
