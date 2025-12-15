package com.mycompany.myapp.service.session;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class EstadoSesionUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private PasosCompra pasoActual;
    private Long eventoIdActual;
    private Set<AsientoSeleccionado> asientosSeleccionados;

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

    public Set<AsientoSeleccionado> getAsientosSeleccionados() {
        return asientosSeleccionados;
    }

    public void setAsientosSeleccionados(Set<AsientoSeleccionado> asientosSeleccionados) {
        this.asientosSeleccionados = asientosSeleccionados;
    }
}
