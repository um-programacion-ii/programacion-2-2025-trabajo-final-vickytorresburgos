package com.mycompany.myapp.service.dto.catedra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mycompany.myapp.service.session.AsientoSeleccionado;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO para recibir la respuesta de la venta desde la Cátedra (Payload 7).
 * Este objeto captura la confirmación de la venta.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RealizarVentaResponse {

    @JsonProperty("ventaId")
    private Long ventaId;

    private Long eventoId;
    private Instant fechaVenta;
    private boolean resultado;
    private String descripcion;
    private BigDecimal precioVenta;

    private List<AsientoSeleccionado> asientos;


    public Long getVentaId() { return ventaId; }
    public void setVentaId(Long ventaId) { this.ventaId = ventaId; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public Instant getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(Instant fechaVenta) { this.fechaVenta = fechaVenta; }

    public boolean isResultado() { return resultado; }
    public void setResultado(boolean resultado) { this.resultado = resultado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }

    public List<AsientoSeleccionado> getAsientos() { return asientos; }
    public void setAsientos(List<AsientoSeleccionado> asientos) { this.asientos = asientos; }
}
