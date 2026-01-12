package org.mycompany.myapp.data

import kotlinx.serialization.Serializable


@Serializable
data class MapaAsientosResponse(
    val eventoId: Long,
    val asientos: List<AsientoEstadoDTO>
)

@Serializable
data class AsientoEstadoDTO(
    val fila: Int,
    val columna: Int,
    val estado: String,
    val expira: String? = null
)

@Serializable
data class AsientoSolicitudDTO(
    val fila: Int,
    val columna: Int
)

@Serializable
data class AsientoVentaItem(
    val fila: Int,
    val columna: Int,
    val nombrePersona: String = ""
)