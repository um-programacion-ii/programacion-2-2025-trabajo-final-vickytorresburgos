package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.EventoTestSamples.*;
import static com.mycompany.myapp.domain.IntegranteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.IntegranteEntity;
import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EventoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventoEntity.class);
        EventoEntity evento1 = getEventoSample1();
        EventoEntity evento2 = new EventoEntity();
        assertThat(evento1).isNotEqualTo(evento2);

        evento2.setId(evento1.getId());
        assertThat(evento1).isEqualTo(evento2);

        evento2 = getEventoSample2();
        assertThat(evento1).isNotEqualTo(evento2);
    }

    @Test
    void integrantesTest() {
        EventoEntity evento = getEventoRandomSampleGenerator();
        IntegranteEntity integranteBack = getIntegranteRandomSampleGenerator();

        evento.addIntegrantes(integranteBack);
        assertThat(evento.getIntegrantes()).containsOnly(integranteBack);
        assertThat(integranteBack.getEvento()).isEqualTo(evento);

        evento.removeIntegrantes(integranteBack);
        assertThat(evento.getIntegrantes()).doesNotContain(integranteBack);
        assertThat(integranteBack.getEvento()).isNull();

        evento.integrantes(new HashSet<>(Set.of(integranteBack)));
        assertThat(evento.getIntegrantes()).containsOnly(integranteBack);
        assertThat(integranteBack.getEvento()).isEqualTo(evento);

        evento.setIntegrantes(new HashSet<>());
        assertThat(evento.getIntegrantes()).doesNotContain(integranteBack);
        assertThat(integranteBack.getEvento()).isNull();
    }
}
