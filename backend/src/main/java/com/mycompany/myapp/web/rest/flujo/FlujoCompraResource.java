package com.mycompany.myapp.web.rest.flujo;

import com.mycompany.myapp.service.FlujoCompraService;
import com.mycompany.myapp.service.SincronizacionService;
import com.mycompany.myapp.service.dto.catedra.RealizarVentaDTO;
import com.mycompany.myapp.service.session.EstadoSesionUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/flujo")
public class FlujoCompraResource {

    private final Logger log = LoggerFactory.getLogger(FlujoCompraResource.class);

    private final SincronizacionService sincronizacionService;
    private final FlujoCompraService flujoCompraService; // <-- NUEVO SERVICIO

    public FlujoCompraResource(
        SincronizacionService sincronizacionService,
        FlujoCompraService flujoCompraService
    ) {
        this.sincronizacionService = sincronizacionService;
        this.flujoCompraService = flujoCompraService;
    }

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

    @PostMapping("/bloquear-asientos")
    public ResponseEntity<EstadoSesionUsuario> bloquearAsientos() {
        log.debug("REST request para bloquear asientos (Delegando a Service)");
        // Toda la lógica está ahora en el servicio
        EstadoSesionUsuario sesion = flujoCompraService.bloquearAsientos();
        return ResponseEntity.ok(sesion);
    }

    @PostMapping("/realizar-venta")
    public ResponseEntity<RealizarVentaDTO> realizarVenta() {
        log.debug("REST request para realizar venta (Delegando a Service)");
        // Toda la lógica está ahora en el servicio
        RealizarVentaDTO respuesta = flujoCompraService.realizarVenta();
        return ResponseEntity.ok(respuesta);
    }
}
