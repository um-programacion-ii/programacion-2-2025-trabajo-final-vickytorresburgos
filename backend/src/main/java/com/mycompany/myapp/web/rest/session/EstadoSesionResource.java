package com.mycompany.myapp.web.rest.session;

import com.mycompany.myapp.service.session.AsientoTemporal;
import com.mycompany.myapp.service.session.EstadoSesionService;
import com.mycompany.myapp.service.session.EstadoSesionUsuario;
import com.mycompany.myapp.service.session.PasosCompra;
import com.mycompany.myapp.web.rest.session.dto.SeleccionarAsientoDTO;
import com.mycompany.myapp.web.rest.session.dto.SeleccionarEventoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller para gestionar el estado de la sesión del usuario.
 */
@RestController
@RequestMapping("/api/sesion")
public class EstadoSesionResource {

    private final Logger log = LoggerFactory.getLogger(EstadoSesionResource.class);
    private final EstadoSesionService estadoSesionService;

    public EstadoSesionResource(EstadoSesionService estadoSesionService) {
        this.estadoSesionService = estadoSesionService;
    }

    /**
     * {@code GET /estado} : Obtiene el estado actual de la sesión del usuario.
     *
     * @return la {@link ResponseEntity} con estado {@code 200 (OK)} y el
     * EstadoSesionUsuario en el cuerpo.
     */
    @GetMapping("/estado")
    public ResponseEntity<EstadoSesionUsuario> getEstadoSesion() {
        EstadoSesionUsuario estadoActual = estadoSesionService.cargarEstado();
        return ResponseEntity.ok(estadoActual);
    }

    /**
     * {@code POST /seleccionar-evento} : Actualiza el evento que el usuario
     * está viendo.
     *
     * @param dto El DTO que contiene el eventoId.
     * @return la {@link ResponseEntity} con estado {@code 200 (OK)} y el
     * estado de sesión actualizado.
     */
    @PostMapping("/seleccionar-evento")
    public ResponseEntity<EstadoSesionUsuario> seleccionarEvento(@RequestBody SeleccionarEventoDTO dto) {
        EstadoSesionUsuario estadoActual = estadoSesionService.cargarEstado();

        estadoActual.setEventoIdActual(dto.eventoId());
        estadoActual.setPasoActual(PasosCompra.SELECCIONANDO_EVENTOS);
        estadoActual.getAsientosSeleccionados().clear();

        estadoSesionService.guardarEstado(estadoActual);

        return ResponseEntity.ok(estadoActual);
    }

    /**
     * {@code POST /agregar-asiento} : Agrega un asiento a la sesión actual.
     * Respeta el límite de 4 asientos.
     */
    @PostMapping("/agregar-asiento")
    public ResponseEntity<EstadoSesionUsuario> agregarAsiento(@RequestBody SeleccionarAsientoDTO dto) {
        log.debug("REST request para agregar asiento: Fila {}, Columna {}", dto.fila(), dto.columna());

        EstadoSesionUsuario estadoActual = estadoSesionService.cargarEstado();

        // Validación: No se pueden seleccionar más de 4 asientos [cite: 59, 164]
        if (estadoActual.getAsientosSeleccionados().size() >= 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden seleccionar más de 4 asientos");
        }

        // Validación: No se puede agregar si no se ha seleccionado un evento
        if (estadoActual.getPasoActual() == PasosCompra.LISTANDO_EVENTOS || estadoActual.getEventoIdActual() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un evento antes de agregar asientos");
        }

        // Creamos el AsientoTemporal (DTO vs Objeto de Sesión)
        AsientoTemporal asiento = new AsientoTemporal(dto.fila(), dto.columna());

        // El 'Set' maneja automáticamente que no se agreguen duplicados
        estadoActual.getAsientosSeleccionados().add(asiento);
        estadoActual.setPasoActual(PasosCompra.SELECCIONANDO_ASIENTOS);

        estadoSesionService.guardarEstado(estadoActual);
        return ResponseEntity.ok(estadoActual);
    }

    /**
     * {@code POST /quitar-asiento} : Quita un asiento de la sesión actual.
     */
    @PostMapping("/quitar-asiento")
    public ResponseEntity<EstadoSesionUsuario> quitarAsiento(@RequestBody SeleccionarAsientoDTO dto) {
        log.debug("REST request para quitar asiento: Fila {}, Columna {}", dto.fila(), dto.columna());

        EstadoSesionUsuario estadoActual = estadoSesionService.cargarEstado();

        AsientoTemporal asiento = new AsientoTemporal(dto.fila(), dto.columna());

        // Quitamos el asiento
        estadoActual.getAsientosSeleccionados().remove(asiento);

        // Si quitamos el último asiento, volvemos al paso anterior
        if (estadoActual.getAsientosSeleccionados().isEmpty()) {
            estadoActual.setPasoActual(PasosCompra.SELECCIONANDO_EVENTOS);
        }

        estadoSesionService.guardarEstado(estadoActual);
        return ResponseEntity.ok(estadoActual);
    }
}
