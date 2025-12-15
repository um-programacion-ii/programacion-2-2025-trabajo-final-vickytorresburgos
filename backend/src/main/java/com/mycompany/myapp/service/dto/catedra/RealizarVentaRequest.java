package com.mycompany.myapp.service.dto.catedra;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.service.session.AsientoSeleccionado;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public class RealizarVentaRequest {

    @JsonProperty("eventoId")
    private Long eventoId;

    @JsonProperty("fecha")
    private Instant fecha;

    @JsonProperty("precioVenta")
    private BigDecimal precioVenta;

    @JsonProperty("asientos")
    private Set<AsientoSeleccionado> asientos;

    // Getters y Setters
    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public Instant getFecha() { return fecha; }
    public void setFecha(Instant fecha) { this.fecha = fecha; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public Set<AsientoSeleccionado> getAsientos() { return asientos; }
    public void setAsientos(Set<AsientoSeleccionado> asientos) { this.asientos = asientos; }
}
