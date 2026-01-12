package com.mycompany.myapp.web.rest.session.dto;

import com.mycompany.myapp.service.session.AsientoSeleccionado;

import java.util.List;


/**
 * DTO para recibir la lista de asientos con los nombres
 * de las personas asignadas.
 */
public record CargarNombresDTO (
    List<AsientoSeleccionado> asientos
) {
}
