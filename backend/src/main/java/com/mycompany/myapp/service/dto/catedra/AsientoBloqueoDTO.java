package com.mycompany.myapp.service.dto.catedra;

public class AsientoBloqueoDTO {
    private int fila;
    private int columna;

    public AsientoBloqueoDTO(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() { return fila; }
    public void setFila(int fila) { this.fila = fila; }
    public int getColumna() { return columna; }
    public void setColumna(int columna) { this.columna = columna; }
}
