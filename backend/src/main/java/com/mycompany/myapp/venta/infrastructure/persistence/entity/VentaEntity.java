package com.mycompany.myapp.venta.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.AsientoVendido;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Entidad que registra una Venta (exitosa o fallida)
 * realizada por un usuario.
 */
@Entity
@Table(name = "venta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VentaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "venta_catedra_id")
    private Long ventaCatedraId;

    @NotNull
    @Column(name = "fecha_venta", nullable = false)
    private Instant fechaVenta;

    @NotNull
    @Column(name = "precio_venta", precision = 21, scale = 2, nullable = false)
    private BigDecimal precioVenta;

    @NotNull
    @Column(name = "resultado", nullable = false)
    private Boolean resultado;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "cantidad_asientos")
    private Integer cantidadAsientos;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "venta" }, allowSetters = true)
    private Set<AsientoVendido> asientos = new HashSet<>();

    /**
     * Se agrega 'with builtInEntity' para linkear
     * a la entidad User de JHipster.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * Una Venta debe estar asociada a un Evento local.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "integrantes" }, allowSetters = true)
    private EventoEntity evento;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VentaEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVentaCatedraId() {
        return this.ventaCatedraId;
    }

    public VentaEntity ventaCatedraId(Long ventaCatedraId) {
        this.setVentaCatedraId(ventaCatedraId);
        return this;
    }

    public void setVentaCatedraId(Long ventaCatedraId) {
        this.ventaCatedraId = ventaCatedraId;
    }

    public Instant getFechaVenta() {
        return this.fechaVenta;
    }

    public VentaEntity fechaVenta(Instant fechaVenta) {
        this.setFechaVenta(fechaVenta);
        return this;
    }

    public void setFechaVenta(Instant fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getPrecioVenta() {
        return this.precioVenta;
    }

    public VentaEntity precioVenta(BigDecimal precioVenta) {
        this.setPrecioVenta(precioVenta);
        return this;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Boolean getResultado() {
        return this.resultado;
    }

    public VentaEntity resultado(Boolean resultado) {
        this.setResultado(resultado);
        return this;
    }

    public void setResultado(Boolean resultado) {
        this.resultado = resultado;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public VentaEntity descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidadAsientos() {
        return this.cantidadAsientos;
    }

    public VentaEntity cantidadAsientos(Integer cantidadAsientos) {
        this.setCantidadAsientos(cantidadAsientos);
        return this;
    }

    public void setCantidadAsientos(Integer cantidadAsientos) {
        this.cantidadAsientos = cantidadAsientos;
    }

    public Set<AsientoVendido> getAsientos() {
        return this.asientos;
    }

    public void setAsientos(Set<AsientoVendido> asientoVendidos) {
        if (this.asientos != null) {
            this.asientos.forEach(i -> i.setVenta(null));
        }
        if (asientoVendidos != null) {
            asientoVendidos.forEach(i -> i.setVenta(this));
        }
        this.asientos = asientoVendidos;
    }

    public VentaEntity asientos(Set<AsientoVendido> asientoVendidos) {
        this.setAsientos(asientoVendidos);
        return this;
    }

    public VentaEntity addAsientos(AsientoVendido asientoVendido) {
        this.asientos.add(asientoVendido);
        asientoVendido.setVenta(this);
        return this;
    }

    public VentaEntity removeAsientos(AsientoVendido asientoVendido) {
        this.asientos.remove(asientoVendido);
        asientoVendido.setVenta(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VentaEntity user(User user) {
        this.setUser(user);
        return this;
    }

    public EventoEntity getEvento() {
        return this.evento;
    }

    public void setEvento(EventoEntity evento) {
        this.evento = evento;
    }

    public VentaEntity evento(EventoEntity evento) {
        this.setEvento(evento);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VentaEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((VentaEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Venta{" +
            "id=" + getId() +
            ", ventaCatedraId=" + getVentaCatedraId() +
            ", fechaVenta='" + getFechaVenta() + "'" +
            ", precioVenta=" + getPrecioVenta() +
            ", resultado='" + getResultado() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", cantidadAsientos=" + getCantidadAsientos() +
            "}";
    }
}
