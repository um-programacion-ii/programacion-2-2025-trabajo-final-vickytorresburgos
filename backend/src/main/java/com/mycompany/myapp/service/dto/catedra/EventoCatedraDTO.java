package com.mycompany.myapp.service.dto.catedra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EventoCatedraDTO {

    @JsonProperty("id")
    private Long eventoCatedraId;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descripcion")
    private String descripcion;

    @JsonProperty("fecha")
    private Instant fecha;

    @JsonProperty("filaAsientos")
    private Integer filaAsientos;

    @JsonProperty("columnAsientos")
    private Integer columnAsientos;

    @JsonProperty("precioEntrada")
    private BigDecimal precioEntrada;

    @JsonProperty("eventoTipo")
    private EventoTipoDTO eventoTipo;

    @JsonProperty("integrantes")
    private List<IntegranteDTO> integrantes;


    public Long getEventoCatedraId() { return eventoCatedraId; }
    public void setEventoCatedraId(Long eventoCatedraId) { this.eventoCatedraId = eventoCatedraId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Instant getFecha() { return fecha; }
    public void setFecha(Instant fecha) { this.fecha = fecha; }
    public Integer getFilaAsientos() { return filaAsientos; }
    public void setFilaAsientos(Integer filaAsientos) { this.filaAsientos = filaAsientos; }
    public Integer getColumnAsientos() { return columnAsientos; }
    public void setColumnAsientos(Integer columnAsientos) { this.columnAsientos = columnAsientos; }
    public BigDecimal getPrecioEntrada() { return precioEntrada; }
    public void setPrecioEntrada(BigDecimal precioEntrada) { this.precioEntrada = precioEntrada; }
    public EventoTipoDTO getEventoTipo() { return eventoTipo; }
    public void setEventoTipo(EventoTipoDTO eventoTipo) { this.eventoTipo = eventoTipo; }
    public List<IntegranteDTO> getIntegrantes() { return integrantes; }
    public void setIntegrantes(List<IntegranteDTO> integrantes) { this.integrantes = integrantes; }
}
