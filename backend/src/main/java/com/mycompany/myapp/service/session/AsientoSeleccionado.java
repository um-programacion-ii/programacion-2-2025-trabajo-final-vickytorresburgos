package com.mycompany.myapp.service.session;

import java.io.Serializable;
import java.util.Objects;

public class AsientoSeleccionado implements Serializable {
    private static final long serialVersionUID = 1L;
    private int fila;
    private int columna;
    private String persona;

    public AsientoSeleccionado() {}

    public AsientoSeleccionado(int fila, int columna, String persona) {
        this.fila = fila;
        this.columna = columna;
        this.persona = persona;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsientoSeleccionado that = (AsientoSeleccionado) o;
        return fila == that.fila && columna == that.columna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fila, columna);
    }
}
