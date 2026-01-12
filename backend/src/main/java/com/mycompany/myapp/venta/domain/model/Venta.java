package com.mycompany.myapp.venta.domain.model;

import com.mycompany.myapp.evento.domain.model.Evento;

import java.math.BigDecimal;
import java.time.Instant;


public class Venta {
    private Long id;
    private Long eventoId;
    private Long userId;
    private BigDecimal precioVenta;
    private Instant fechaVenta;
    private Boolean resultado;

    private Long ventaCatedraId;
    private String descripcion;
    private Integer cantidadAsientos;
    private String userLogin;
    private String eventoTitulo;
    private Evento evento;

    public Venta() {
    }

    public Venta(Long id, Long eventoId, Long userId, BigDecimal precioVenta, Instant fechaVenta, Boolean resultado, Long ventaCatedraId, String descripcion, Integer cantidadAsientos, String userLogin, String eventoTitulo) {
        this.id = id;
        this.eventoId = eventoId;
        this.userId = userId;
        this.precioVenta = precioVenta;
        this.fechaVenta = fechaVenta;
        this.resultado = resultado;
        this.ventaCatedraId = ventaCatedraId;
        this.descripcion = descripcion;
        this.cantidadAsientos = cantidadAsientos;
        this.userLogin = userLogin;
        this.eventoTitulo = eventoTitulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventoId() {
        return eventoId;
    }

    public void setEventoId(Long eventoId) {
        this.eventoId = eventoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Instant getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Boolean getResultado() {
        return resultado;
    }

    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

    public Long getVentaCatedraId() {
        return ventaCatedraId;
    }

    public void setVentaCatedraId(Long ventaCatedraId) {
        this.ventaCatedraId = ventaCatedraId;
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

    public String getEventoTitulo() {
        return eventoTitulo;
    }

    public void setEventoTitulo(String eventoTitulo) {
        this.eventoTitulo = eventoTitulo;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public void confirmar() {
        this.resultado = true;
        this.fechaVenta = Instant.now();
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
