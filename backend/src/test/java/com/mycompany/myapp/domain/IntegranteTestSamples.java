package com.mycompany.myapp.domain;

import com.mycompany.myapp.evento.infrastructure.persistence.entity.IntegranteEntity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class IntegranteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static IntegranteEntity getIntegranteSample1() {
        return new IntegranteEntity().id(1L).nombre("nombre1").apellido("apellido1").identificacion("identificacion1");
    }

    public static IntegranteEntity getIntegranteSample2() {
        return new IntegranteEntity().id(2L).nombre("nombre2").apellido("apellido2").identificacion("identificacion2");
    }

    public static IntegranteEntity getIntegranteRandomSampleGenerator() {
        return new IntegranteEntity()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellido(UUID.randomUUID().toString())
            .identificacion(UUID.randomUUID().toString());
    }
}
