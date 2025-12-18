package org.mycompany.myapp.data

import kotlinx.serialization.Serializable

@Serializable
data class BloqueoRequest(
    val eventoId: Long,
    val asientos: List<AsientoSolicitudDTO>
)