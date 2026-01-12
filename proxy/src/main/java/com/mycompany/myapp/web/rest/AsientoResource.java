package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.AsientoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AsientoResource {

    private final Logger log = LoggerFactory.getLogger(AsientoResource.class);

    private final AsientoService asientoService;

    public AsientoResource(AsientoService asientoService) {
        this.asientoService = asientoService;
    }

    /**
     * {@code GET /asientos/:eventoId} : Obtiene el mapa de asientos (como String/JSON)
     * desde el Redis de la Cátedra.
     *
     * @param eventoId el ID del evento.
     * @return el String (JSON) con el mapa de asientos.
     */
    @GetMapping("/asientos/{eventoId}")
    public ResponseEntity<String> getMapaAsientos(@PathVariable Long eventoId) {
        log.debug("REST request (proxy) para obtener mapa de asientos del evento: {}", eventoId);
        String mapaAsientos = asientoService.getMapaAsientos(eventoId);
        return ResponseEntity.ok(mapaAsientos);
    }
}
