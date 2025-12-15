package com.mycompany.myapp.service.dto.catedra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BloquearAsientosResponse {
    @JsonProperty("resultado")
    private boolean resultado;

    @JsonProperty("descripcion")
    private String descripcion;
    private Long eventoId;
    private List<AsientoBloqueoDTO> asientos;

    public boolean isResultado() { return resultado; }
    public void setResultado(boolean resultado) { this.resultado = resultado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }

    public List<AsientoBloqueoDTO> getAsientos() { return asientos; }
    public void setAsientos(List<AsientoBloqueoDTO> asientos) { this.asientos = asientos; }

}

