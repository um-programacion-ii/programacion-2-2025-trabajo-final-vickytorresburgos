package com.mycompany.myapp.domain;

import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static EventoEntity getEventoSample1() {
        return new EventoEntity()
            .id(1L)
            .eventoCatedraId(1L)
            .titulo("titulo1")
            .resumen("resumen1")
            .descripcion("descripcion1")
            .direccion("direccion1")
            .imagen("imagen1")
            .filaAsientos(1)
            .columnAsientos(1)
            .eventoTipoNombre("eventoTipoNombre1")
            .eventoTipoDescripcion("eventoTipoDescripcion1");
    }

    public static EventoEntity getEventoSample2() {
        return new EventoEntity()
            .id(2L)
            .eventoCatedraId(2L)
            .titulo("titulo2")
            .resumen("resumen2")
            .descripcion("descripcion2")
            .direccion("direccion2")
            .imagen("imagen2")
            .filaAsientos(2)
            .columnAsientos(2)
            .eventoTipoNombre("eventoTipoNombre2")
            .eventoTipoDescripcion("eventoTipoDescripcion2");
    }

    public static EventoEntity getEventoRandomSampleGenerator() {
        return new EventoEntity()
            .id(longCount.incrementAndGet())
            .eventoCatedraId(longCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .resumen(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .imagen(UUID.randomUUID().toString())
            .filaAsientos(intCount.incrementAndGet())
            .columnAsientos(intCount.incrementAndGet())
            .eventoTipoNombre(UUID.randomUUID().toString())
            .eventoTipoDescripcion(UUID.randomUUID().toString());
    }
}
