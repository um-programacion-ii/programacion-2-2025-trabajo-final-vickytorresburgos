package com.mycompany.myapp.service.dto.catedra;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatedraAsientoVendidoDTO {

    private int fila;
    private int columna;
    private String persona;
    private String estado;

    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }
    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna; }
    public String getPersona() { return persona; }
    public void setPersona(String persona) { this.persona = persona; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
