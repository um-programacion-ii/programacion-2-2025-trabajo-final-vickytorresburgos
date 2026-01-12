package org.mycompany.myapp.data

import kotlinx.serialization.Serializable
import org.mycompany.myapp.utils.DateUtils

@Serializable
data class EventoTipoDTO(
    val nombre: String? = null,
    val descripcion: String? = null
)

@Serializable
data class IntegranteDTO(
    val nombre: String? = null,
    val apellido: String? = null,
    val identificacion: String? = null
)
@Serializable
data class EventoDTO(
    val id: Long? = null,
    val titulo: String? = null,
    val resumen: String? = null,
    val descripcion: String? = null,
    val fecha: String? = null,
    val direccion: String? = null,
    val imagen: String? = null,
    val precioEntrada: Double? = null,

    val eventoTipo: EventoTipoDTO? = null,

    val filaAsientos: Int? = 0,
    val columnAsientos: Int? = 0,

    val asientosDisponibles: Int? = 0,
    val asientosOcupados: Int? = 0,

    val capacidadTotal: Int? = 0,

    val integrantes: List<IntegranteDTO>? = null
) {
    val fechaFormateada: String
        get() = DateUtils.formatearFecha(fecha)

    val nombreCompletoIntegrantes: String
        get() = integrantes?.joinToString { "${it.nombre} ${it.apellido} (${it.identificacion})" } ?: "Sin integrantes"
}