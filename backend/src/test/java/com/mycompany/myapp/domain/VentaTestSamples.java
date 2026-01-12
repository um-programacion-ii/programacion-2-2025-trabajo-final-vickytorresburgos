package com.mycompany.myapp.domain;

import com.mycompany.myapp.venta.infrastructure.persistence.entity.VentaEntity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VentaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VentaEntity getVentaSample1() {
        return new VentaEntity().id(1L).ventaCatedraId(1L).descripcion("descripcion1").cantidadAsientos(1);
    }

    public static VentaEntity getVentaSample2() {
        return new VentaEntity().id(2L).ventaCatedraId(2L).descripcion("descripcion2").cantidadAsientos(2);
    }

    public static VentaEntity getVentaRandomSampleGenerator() {
        return new VentaEntity()
            .id(longCount.incrementAndGet())
            .ventaCatedraId(longCount.incrementAndGet())
            .descripcion(UUID.randomUUID().toString())
            .cantidadAsientos(intCount.incrementAndGet());
    }
}
