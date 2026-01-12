package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EventoTestSamples.*;
import static com.mycompany.myapp.domain.IntegranteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.IntegranteEntity;
import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IntegranteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IntegranteEntity.class);
        IntegranteEntity integrante1 = getIntegranteSample1();
        IntegranteEntity integrante2 = new IntegranteEntity();
        assertThat(integrante1).isNotEqualTo(integrante2);

        integrante2.setId(integrante1.getId());
        assertThat(integrante1).isEqualTo(integrante2);

        integrante2 = getIntegranteSample2();
        assertThat(integrante1).isNotEqualTo(integrante2);
    }

    @Test
    void eventoTest() {
        IntegranteEntity integrante = getIntegranteRandomSampleGenerator();
        EventoEntity eventoBack = getEventoRandomSampleGenerator();

        integrante.setEvento(eventoBack);
        assertThat(integrante.getEvento()).isEqualTo(eventoBack);

        integrante.evento(null);
        assertThat(integrante.getEvento()).isNull();
    }
}
