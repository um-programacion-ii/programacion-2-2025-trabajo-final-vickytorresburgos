package com.mycompany.myapp.service.dto.catedra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BloquearAsientosResponse {
    @JsonProperty("resultado")
    private boolean resultado;

    @JsonProperty("descripcion")
    private String descripcion;

    public boolean isResultado() { return resultado; }
    public void setResultado(boolean resultado) { this.resultado = resultado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}

