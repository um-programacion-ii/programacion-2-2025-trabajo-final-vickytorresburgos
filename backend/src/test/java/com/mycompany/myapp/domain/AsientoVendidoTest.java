package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AsientoVendidoTestSamples.*;
import static com.mycompany.myapp.domain.VentaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.venta.infrastructure.persistence.entity.VentaEntity;
import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AsientoVendidoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AsientoVendido.class);
        AsientoVendido asientoVendido1 = getAsientoVendidoSample1();
        AsientoVendido asientoVendido2 = new AsientoVendido();
        assertThat(asientoVendido1).isNotEqualTo(asientoVendido2);

        asientoVendido2.setId(asientoVendido1.getId());
        assertThat(asientoVendido1).isEqualTo(asientoVendido2);

        asientoVendido2 = getAsientoVendidoSample2();
        assertThat(asientoVendido1).isNotEqualTo(asientoVendido2);
    }

    @Test
    void ventaTest() {
        AsientoVendido asientoVendido = getAsientoVendidoRandomSampleGenerator();
        VentaEntity ventaBack = getVentaRandomSampleGenerator();

        asientoVendido.setVenta(ventaBack);
        assertThat(asientoVendido.getVenta()).isEqualTo(ventaBack);

        asientoVendido.venta(null);
        assertThat(asientoVendido.getVenta()).isNull();
    }
}
