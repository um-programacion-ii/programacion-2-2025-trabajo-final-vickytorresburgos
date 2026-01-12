package com.mycompany.myapp.web.rest.session;

import com.mycompany.myapp.service.session.AsientoSeleccionado;
import com.mycompany.myapp.service.session.EstadoSesionService;
import com.mycompany.myapp.service.session.EstadoSesionUsuario;
import com.mycompany.myapp.service.session.PasosCompra;
import com.mycompany.myapp.web.rest.session.dto.CargarNombresDTO;
import com.mycompany.myapp.web.rest.session.dto.SeleccionarAsientoDTO;
import com.mycompany.myapp.web.rest.session.dto.SeleccionarEventoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;

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
     * {@code POST /logout} : Limpia la sesión de Redis del usuario.
     *
     * @return la {@link ResponseEntity} con estado {@code 200 (OK)}.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        estadoSesionService.limpiarEstado();
        return ResponseEntity.ok().build();
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

        if (estadoActual.getAsientosSeleccionados().size() >= 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pueden seleccionar más de 4 asientos");
        }

        if (estadoActual.getPasoActual() == PasosCompra.LISTANDO_EVENTOS || estadoActual.getEventoIdActual() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un evento antes de agregar asientos");
        }

        AsientoSeleccionado asiento = new AsientoSeleccionado(dto.fila(), dto.columna(), null);

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

        AsientoSeleccionado asiento = new AsientoSeleccionado(dto.fila(), dto.columna(), null);

        estadoActual.getAsientosSeleccionados().remove(asiento);

        if (estadoActual.getAsientosSeleccionados().isEmpty()) {
            estadoActual.setPasoActual(PasosCompra.SELECCIONANDO_EVENTOS);
        }

        estadoSesionService.guardarEstado(estadoActual);
        return ResponseEntity.ok(estadoActual);
    }

    /**
     * {@code POST /cargar-nombres} : Recibe la lista de asientos con nombres
     * y actualiza la sesión.
     *
     */
    @PostMapping("/cargar-nombres")
    public ResponseEntity<EstadoSesionUsuario> cargarNombres(@RequestBody CargarNombresDTO dto) {
        log.debug("REST request para cargar nombres en {} asientos", dto.asientos().size());

        EstadoSesionUsuario estadoActual = estadoSesionService.cargarEstado();

        if (estadoActual.getPasoActual() == PasosCompra.LISTANDO_EVENTOS || estadoActual.getEventoIdActual() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un evento antes de cargar nombres");
        }

        estadoActual.setAsientosSeleccionados(new HashSet<>(dto.asientos()));
        estadoActual.setPasoActual(PasosCompra.CARGANDO_NOMBRES);

        estadoSesionService.guardarEstado(estadoActual);
        return ResponseEntity.ok(estadoActual);
    }
}
