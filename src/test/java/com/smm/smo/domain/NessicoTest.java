package com.smm.smo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.smm.smo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NessicoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nessico.class);
        Nessico nessico1 = new Nessico();
        nessico1.setId(1L);
        Nessico nessico2 = new Nessico();
        nessico2.setId(nessico1.getId());
        assertThat(nessico1).isEqualTo(nessico2);
        nessico2.setId(2L);
        assertThat(nessico1).isNotEqualTo(nessico2);
        nessico1.setId(null);
        assertThat(nessico1).isNotEqualTo(nessico2);
    }
}
