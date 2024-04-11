package com.yuzhi.ainms.core.domain;

import static com.yuzhi.ainms.core.domain.NCEAPStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.yuzhi.ainms.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NCEAPStatusTest {

  @Test
  void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(NCEAPStatus.class);
    NCEAPStatus nCEAPStatus1 = getNCEAPStatusSample1();
    NCEAPStatus nCEAPStatus2 = new NCEAPStatus();
    assertThat(nCEAPStatus1).isNotEqualTo(nCEAPStatus2);

    nCEAPStatus2.setId(nCEAPStatus1.getId());
    assertThat(nCEAPStatus1).isEqualTo(nCEAPStatus2);

    nCEAPStatus2 = getNCEAPStatusSample2();
    assertThat(nCEAPStatus1).isNotEqualTo(nCEAPStatus2);
  }
}
