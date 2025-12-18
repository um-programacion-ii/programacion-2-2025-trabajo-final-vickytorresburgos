package org.mycompany.myapp.data

import kotlinx.serialization.Serializable

@Serializable
data class RealizarVentaRequest(
    val eventoId: Long,
    val asientos: List<AsientoVentaItem>
)

@Serializable
data class VentaDTO(
    val id: Long,
    val fechaVenta: String? = null,
    val precioVenta: Double? = null,
    val cantidadAsientos: Int? = 0,
    val descripcion: String? = null,
    val tickets: List<TicketDetalleDTO>? = null, // Lista de entradas
    val evento: EventoDTO? = null
)

@Serializable
data class TicketDetalleDTO(
    val fila: Int,
    val columna: Int,
    val nombrePersona: String? = null
)
