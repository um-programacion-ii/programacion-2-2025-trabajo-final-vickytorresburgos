package com.mycompany.myapp.web.rest.flujo;

import com.mycompany.myapp.service.SincronizacionService;
import com.mycompany.myapp.service.dto.catedra.AsientoBloqueoDTO;
import com.mycompany.myapp.service.dto.catedra.BloquearAsientosRequest;
import com.mycompany.myapp.service.dto.catedra.BloquearAsientosResponse;
import com.mycompany.myapp.service.session.EstadoSesionService;
import com.mycompany.myapp.service.session.EstadoSesionUsuario;
import com.mycompany.myapp.service.session.PasosCompra;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/flujo")
public class FlujoCompraResource {

    private final Logger log = LoggerFactory.getLogger(FlujoCompraResource.class);

    private final EstadoSesionService estadoSesionService;
    private final RestTemplate catedraRestTemplate;
    private final SincronizacionService sincronizacionService;

    public FlujoCompraResource(
        EstadoSesionService estadoSesionService,
        @Qualifier("catedraRestTemplate") RestTemplate catedraRestTemplate, SincronizacionService sincronizacionService
    ) {
        this.estadoSesionService = estadoSesionService;
        this.catedraRestTemplate = catedraRestTemplate;
        this.sincronizacionService = sincronizacionService;
    }

    /**
     * {@code POST /bloquear-asientos} : Confirma los nombres y bloquea
     * los asientos contra la Cátedra.
     *
     * @return El estado de sesión actualizado (si fue exitoso).
     */
    @PostMapping("/bloquear-asientos")
    public ResponseEntity<EstadoSesionUsuario> bloquearAsientos() {
        log.debug("REST request para bloquear asientos en Cátedra");

        EstadoSesionUsuario sesion = estadoSesionService.cargarEstado();

        if (sesion.getPasoActual() != PasosCompra.CARGANDO_NOMBRES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El flujo no está en el paso de cargar nombres");
        }
        if (sesion.getEventoIdActual() == null || sesion.getAsientosSeleccionados().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay asientos seleccionados en la sesión");
        }

        BloquearAsientosRequest requestDTO = new BloquearAsientosRequest();
        requestDTO.setEventoId(sesion.getEventoIdActual());
        requestDTO.setAsientos(
            sesion
                .getAsientosSeleccionados()
                .stream()
                .map(asiento -> new AsientoBloqueoDTO(asiento.getFila(), asiento.getColumna()))
                .collect(Collectors.toList())
        );

        String urlCatedra = "/api/endpoints/v1/bloquear-asientos";
        BloquearAsientosResponse respuestaCatedra;

        try {
            respuestaCatedra = catedraRestTemplate.postForObject(urlCatedra, requestDTO, BloquearAsientosResponse.class);
        } catch (Exception e) {
            log.error("Error al llamar a /bloquear-asientos de Cátedra: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al comunicarse con el servicio de Cátedra");
        }

        if (respuestaCatedra == null || !respuestaCatedra.isResultado()) {
            log.warn("Cátedra rechazó el bloqueo: {}", (respuestaCatedra != null ? respuestaCatedra.getDescripcion() : "Sin respuesta"));
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los asientos ya no están disponibles");
        }

        log.debug("Asientos bloqueados exitosamente en Cátedra");
        sesion.setPasoActual(PasosCompra.VENTA);
        estadoSesionService.guardarEstado(sesion);

        return ResponseEntity.ok(sesion);
    }
    /**
     * {@code POST /sincronizar} : Endpoint de prueba para forzar
     * la sincronización de eventos.
     */
    @PostMapping("/sincronizar")
    public ResponseEntity<String> sincronizar() {
        log.debug("REST request para forzar sincronización de eventos");
        try {
            sincronizacionService.sincronizarEventos();
            return ResponseEntity.ok("Sincronización completada.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error durante la sincronización: " + e.getMessage());
        }
    }
}
