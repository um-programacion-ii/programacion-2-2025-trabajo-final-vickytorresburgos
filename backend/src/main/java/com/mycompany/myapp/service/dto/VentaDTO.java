package com.mycompany.myapp.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Venta} entity.
 */
@Schema(description = "Entidad que registra una Venta (exitosa o fallida)\nrealizada por un usuario.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentaDTO implements Serializable {

    private Long id;

    private Long ventaCatedraId;

    @NotNull
    private Instant fechaVenta;

    @NotNull
    private BigDecimal precioVenta;

    @NotNull
    private Boolean resultado;

    private String descripcion;

    private Integer cantidadAsientos;

    @Schema(description = "Se agrega 'with builtInEntity' para linkear\na la entidad User de JHipster.")
    private UserDTO user;

    @Schema(description = "Una Venta debe estar asociada a un Evento local.")
    private EventoDTO evento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVentaCatedraId() {
        return ventaCatedraId;
    }

    public void setVentaCatedraId(Long ventaCatedraId) {
        this.ventaCatedraId = ventaCatedraId;
    }

    public Instant getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Boolean getResultado() {
        return resultado;
    }

    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadAsientos() {
        return cantidadAsientos;
    }

    public void setCantidadAsientos(Integer cantidadAsientos) {
        this.cantidadAsientos = cantidadAsientos;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public EventoDTO getEvento() {
        return evento;
    }

    public void setEvento(EventoDTO evento) {
        this.evento = evento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentaDTO)) {
            return false;
        }

        VentaDTO ventaDTO = (VentaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ventaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VentaDTO{" +
            "id=" + getId() +
            ", ventaCatedraId=" + getVentaCatedraId() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", precioVenta=" + getPrecioVenta() +
            ", resultado='" + getResultado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", cantidadAsientos=" + getCantidadAsientos() +
            ", user=" + getUser() +
            ", evento=" + getEvento() +
            "}";
    }
}
