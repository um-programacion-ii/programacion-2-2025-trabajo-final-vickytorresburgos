package com.mycompany.myapp.service.session;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class EstadoSesionUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private PasosCompra pasoActual;
    private Long eventoIdActual;
    private Set<AsientoTemporal> asientosSeleccionados;

    public EstadoSesionUsuario() {
        this.pasoActual = PasosCompra.LISTANDO_EVENTOS;
        this.asientosSeleccionados = new HashSet<>();
    }

    public PasosCompra getPasoActual() {
        return pasoActual;
    }

    public void setPasoActual(PasosCompra pasoActual) {
        this.pasoActual = pasoActual;
    }

    public Long getEventoIdActual() {
        return eventoIdActual;
    }

    public void setEventoIdActual(Long eventoIdActual) {
        this.eventoIdActual = eventoIdActual;
    }

    public Set<AsientoTemporal> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    public void setAsientosSeleccionados(Set<AsientoTemporal> asientosSeleccionados) {
        this.asientosSeleccionados = asientosSeleccionados;
    }
}
