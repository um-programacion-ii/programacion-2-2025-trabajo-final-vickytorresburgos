package com.mycompany.myapp.evento.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evento {
    private Long id;
    private Long eventoCatedraId;
    private String titulo;
    private String resumen;
    private String descripcion;
    private Instant fecha;
    private String direccion;
    private String imagen;
    private Integer filaAsientos;
    private Integer columnAsientos;
    private BigDecimal precioEntrada;
    private String eventoTipoNombre;
    private String eventoTipoDescripcion;
    private List<Integrante> integrantes;

    public Evento() {
    }

    public Evento(Long id,
                  Long eventoCatedraId,
                  String titulo,
                  String resumen,
                  String descripcion,
                  Instant fecha,
                  String direccion,
                  String imagen,
                  Integer filaAsientos,
                  Integer columnAsientos,
                  BigDecimal precioEntrada,
                  String eventoTipoNombre,
                  String eventoTipoDescripcion,
                  List<Integrante> integrantes) {
        this.id = id;
        this.eventoCatedraId = eventoCatedraId;
        this.titulo = titulo;
        this.resumen = resumen;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.direccion = direccion;
        this.imagen = imagen;
        this.filaAsientos = filaAsientos;
        this.columnAsientos = columnAsientos;
        this.precioEntrada = precioEntrada;
        this.eventoTipoNombre = eventoTipoNombre;
        this.eventoTipoDescripcion = eventoTipoDescripcion;
        this.integrantes = integrantes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventoCatedraId() {
        return eventoCatedraId;
    }

    public void setEventoCatedraId(Long eventoCatedraId) {
        this.eventoCatedraId = eventoCatedraId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getFilaAsientos() {
        return filaAsientos;
    }

    public void setFilaAsientos(Integer filaAsientos) {
        this.filaAsientos = filaAsientos;
    }

    public Integer getColumnAsientos() {
        return columnAsientos;
    }

    public void setColumnAsientos(Integer columnAsientos) {
        this.columnAsientos = columnAsientos;
    }

    public BigDecimal getPrecioEntrada() {
        return precioEntrada;
    }

    public void setPrecioEntrada(BigDecimal precioEntrada) {
        this.precioEntrada = precioEntrada;
    }

    public String getEventoTipoNombre() {
        return eventoTipoNombre;
    }

    public void setEventoTipoNombre(String eventoTipoNombre) {
        this.eventoTipoNombre = eventoTipoNombre;
    }

    public String getEventoTipoDescripcion() {
        return eventoTipoDescripcion;
    }

    public void setEventoTipoDescripcion(String eventoTipoDescripcion) {
        this.eventoTipoDescripcion = eventoTipoDescripcion;
    }

    public List<Integrante> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Integrante> integrantes) {
        this.integrantes = integrantes;
    }
}
