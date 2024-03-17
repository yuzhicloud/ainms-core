package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.AccessControllerTestSamples.*;
import static com.yuzhi.ainms.core.domain.AccessPointGroupTestSamples.*;
import static com.yuzhi.ainms.core.domain.AccessPointTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void controllerTest() throws Exception {
        AccessPoint accessPoint = getAccessPointRandomSampleGenerator();
        AccessController accessControllerBack = getAccessControllerRandomSampleGenerator();

        accessPoint.setController(accessControllerBack);
        assertThat(accessPoint.getController()).isEqualTo(accessControllerBack);

        accessPoint.controller(null);
        assertThat(accessPoint.getController()).isNull();
    }

    @Test
    void groupTest() throws Exception {
        AccessPoint accessPoint = getAccessPointRandomSampleGenerator();
        AccessPointGroup accessPointGroupBack = getAccessPointGroupRandomSampleGenerator();

        accessPoint.addGroup(accessPointGroupBack);
        assertThat(accessPoint.getGroups()).containsOnly(accessPointGroupBack);
        assertThat(accessPointGroupBack.getAccessPoints()).containsOnly(accessPoint);

        accessPoint.removeGroup(accessPointGroupBack);
        assertThat(accessPoint.getGroups()).doesNotContain(accessPointGroupBack);
        assertThat(accessPointGroupBack.getAccessPoints()).doesNotContain(accessPoint);

        accessPoint.groups(new HashSet<>(Set.of(accessPointGroupBack)));
        assertThat(accessPoint.getGroups()).containsOnly(accessPointGroupBack);
        assertThat(accessPointGroupBack.getAccessPoints()).containsOnly(accessPoint);

        accessPoint.setGroups(new HashSet<>());
        assertThat(accessPoint.getGroups()).doesNotContain(accessPointGroupBack);
        assertThat(accessPointGroupBack.getAccessPoints()).doesNotContain(accessPoint);
    }
}
