package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.SincronizacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller para recibir notificaciones.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionResource {

    private final Logger log = LoggerFactory.getLogger(NotificacionResource.class);

    private final SincronizacionService sincronizacionService;

    public NotificacionResource(SincronizacionService sincronizacionService) {
        this.sincronizacionService = sincronizacionService;
    }

    /**
     * {@code POST /evento-actualizado} : Recibe una notificación de que un evento cambió
     * y dispara la sincronización local.
     *
     * @return la {@link ResponseEntity} con estado {@code 200 (OK)}.
     */
    @PostMapping("/evento-actualizado")
    public ResponseEntity<Void> notificarEventoActualizado() {
        try {
            sincronizacionService.sincronizarEventos();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error durante la sincronización por notificación: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
