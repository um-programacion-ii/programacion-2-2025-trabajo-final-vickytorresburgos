package com.mycompany.myapp.service.dto.catedra;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class BloquearAsientosRequest {

    @JsonProperty("eventoId")
    private Long eventoId;

    @JsonProperty("asientos")
    private List<AsientoBloqueoDTO> asientos;

    public Long getEventoId() { return eventoId; }
    public void setEventoId(Long eventoId) { this.eventoId = eventoId; }
    public List<AsientoBloqueoDTO> getAsientos() { return asientos; }
    public void setAsientos(List<AsientoBloqueoDTO> asientos) { this.asientos = asientos; }
}
