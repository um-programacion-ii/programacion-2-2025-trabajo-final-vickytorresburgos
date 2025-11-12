package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidad que detalla un asiento específico vendido
 * como parte de una Venta.
 */
@Entity
@Table(name = "asiento_vendido")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AsientoVendido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fila", nullable = false)
    private Integer fila;

    @NotNull
    @Column(name = "columna", nullable = false)
    private Integer columna;

    @Column(name = "persona")
    private String persona;

    @Column(name = "estado")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "asientos", "user", "evento" }, allowSetters = true)
    private Venta venta;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AsientoVendido id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFila() {
        return this.fila;
    }

    public AsientoVendido fila(Integer fila) {
        this.setFila(fila);
        return this;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Integer getColumna() {
        return this.columna;
    }

    public AsientoVendido columna(Integer columna) {
        this.setColumna(columna);
        return this;
    }

    public void setColumna(Integer columna) {
        this.columna = columna;
    }

    public String getPersona() {
        return this.persona;
    }

    public AsientoVendido persona(String persona) {
        this.setPersona(persona);
        return this;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getEstado() {
        return this.estado;
    }

    public AsientoVendido estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Venta getVenta() {
        return this.venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public AsientoVendido venta(Venta venta) {
        this.setVenta(venta);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AsientoVendido)) {
            return false;
        }
        return getId() != null && getId().equals(((AsientoVendido) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AsientoVendido{" +
            "id=" + getId() +
            ", fila=" + getFila() +
            ", columna=" + getColumna() +
            ", persona='" + getPersona() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
