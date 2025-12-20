package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AsientoVendido;
import com.mycompany.myapp.evento.domain.model.Evento;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.venta.infrastructure.persistence.entity.VentaEntity;
import com.mycompany.myapp.repository.AsientoVendidoRepository;
import com.mycompany.myapp.evento.infrastructure.persistence.repository.JpaEventoRepository;
import com.mycompany.myapp.venta.infrastructure.persistence.repository.JpaVentaRepository;
import com.mycompany.myapp.service.FlujoCompraService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.service.dto.catedra.*;
import com.mycompany.myapp.service.session.AsientoSeleccionado;
import com.mycompany.myapp.service.session.EstadoSesionService;
import com.mycompany.myapp.service.session.EstadoSesionUsuario;
import com.mycompany.myapp.service.session.PasosCompra;
import com.mycompany.myapp.web.rest.errors.CatedraException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlujoCompraServiceImpl implements FlujoCompraService {

    private final Logger log = LoggerFactory.getLogger(FlujoCompraServiceImpl.class);

    private final EstadoSesionService estadoSesionService;
    private final RestTemplate catedraRestTemplate;
    private final JpaEventoRepository eventoRepository;
    private final JpaVentaRepository ventaRepository;
    private final AsientoVendidoRepository asientoVendidoRepository;
    private final UserService userService;

    public FlujoCompraServiceImpl(
        EstadoSesionService estadoSesionService,
        @Qualifier("catedraRestTemplate") RestTemplate catedraRestTemplate,
        JpaEventoRepository eventoRepository,
        JpaVentaRepository ventaRepository,
        AsientoVendidoRepository asientoVendidoRepository,
        UserService userService
    ) {
        this.estadoSesionService = estadoSesionService;
        this.catedraRestTemplate = catedraRestTemplate;
        this.eventoRepository = eventoRepository;
        this.ventaRepository = ventaRepository;
        this.asientoVendidoRepository = asientoVendidoRepository;
        this.userService = userService;
    }

    @Override
    public BloquearAsientosResponse bloquearAsientos(CatedraBloqueoRequest request) {
        log.debug("Service request para bloquear asientos en Cátedra: {}", request);

        EventoEntity eventoLocal = eventoRepository.findById(request.getEventoId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Evento no encontrado en el sistema local"));

        Long idRealCatedra = eventoLocal.getEventoCatedraId();
        log.debug("Traduciendo ID para bloqueo: Interno {} -> Cátedra {}", request.getEventoId(), idRealCatedra);

        EstadoSesionUsuario sesion = estadoSesionService.cargarEstado();

        sesion.setEventoIdActual(request.getEventoId());

        Set<AsientoSeleccionado> nuevosAsientos = request.getAsientos().stream()
            .map(a -> {
                AsientoSeleccionado as = new AsientoSeleccionado();
                as.setFila(a.getFila());
                as.setColumna(a.getColumna());
                return as;
            })
            .collect(Collectors.toSet());

        sesion.setAsientosSeleccionados(nuevosAsientos);

        CatedraBloqueoRequest requestCatedra = new CatedraBloqueoRequest();

        requestCatedra.setEventoId(idRealCatedra);

        requestCatedra.setAsientos(
            sesion.getAsientosSeleccionados().stream()
                .map(asiento -> new AsientoBloqueoDTO(asiento.getFila(), asiento.getColumna()))
                .collect(Collectors.toList())
        );

        String urlCatedra = "/api/endpoints/v1/bloquear-asientos";
        BloquearAsientosResponse respuestaCatedra;

        try {
            respuestaCatedra = catedraRestTemplate.postForObject(urlCatedra, requestCatedra, BloquearAsientosResponse.class);
        } catch (Exception e) {
            log.error("Error al bloquear: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error de comunicación con Cátedra");
        }

        if (respuestaCatedra == null || !respuestaCatedra.isResultado()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                respuestaCatedra != null ? respuestaCatedra.getDescripcion() : "Asientos ocupados");
        }

        sesion.setPasoActual(PasosCompra.CARGANDO_NOMBRES);
        estadoSesionService.guardarEstado(sesion);

        return respuestaCatedra;
    }

    @Override
    public RealizarVentaDTO realizarVenta() {
        log.info("Service request para realizar venta.");

        EstadoSesionUsuario sesion = estadoSesionService.cargarEstado();

        if (sesion.getAsientosSeleccionados().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay asientos.");
        }

        EventoEntity eventoLocal = eventoRepository
            .findById(sesion.getEventoIdActual())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evento no encontrado localmente."));

        BigDecimal precioTotal = eventoLocal.getPrecioEntrada().multiply(BigDecimal.valueOf(sesion.getAsientosSeleccionados().size()));

        VentaEntity ventaLocal = new VentaEntity();
        ventaLocal.setEvento(eventoLocal);
        ventaLocal.setUser(userService.getUserWithAuthorities().orElse(null));
        ventaLocal.setFechaVenta(Instant.now());
        ventaLocal.setPrecioVenta(precioTotal);
        ventaLocal.setCantidadAsientos(sesion.getAsientosSeleccionados().size());
        ventaLocal.setResultado(false);
        ventaLocal.setDescripcion("Iniciando transacción con Cátedra...");

        VentaEntity ventaGuardada = ventaRepository.save(ventaLocal);

        RealizarVentaRequest requestDTO = new RealizarVentaRequest();
        requestDTO.setEventoId(eventoLocal.getEventoCatedraId());
        requestDTO.setFecha(Instant.now());
        requestDTO.setPrecioVenta(precioTotal);
        requestDTO.setAsientos(sesion.getAsientosSeleccionados());

        RealizarVentaDTO respuestaCatedra;
        try {
            String urlCatedra = "/api/endpoints/v1/realizar-venta";
            respuestaCatedra = catedraRestTemplate.postForObject(urlCatedra, requestDTO, RealizarVentaDTO.class);
        } catch (Exception e) {
            log.error("Error crítico al llamar a Cátedra: {}", e.getMessage());
            ventaGuardada.setResultado(false);
            ventaGuardada.setDescripcion("Fallo técnico " + e.getMessage());
            ventaRepository.save(ventaGuardada);
            throw e;
        }
        if (respuestaCatedra == null) {
            throw new CatedraException("La catedra no responde", HttpStatus.BAD_GATEWAY);
        }

        ventaGuardada.setVentaCatedraId(respuestaCatedra.getVentaId());
        ventaGuardada.setResultado(respuestaCatedra.isResultado());
        ventaGuardada.setDescripcion(respuestaCatedra.getDescripcion());
        ventaRepository.save(ventaGuardada);

        if (respuestaCatedra.isResultado()) {
            Set<AsientoVendido> asientosVendidos = new HashSet<>();
            for (AsientoSeleccionado as : sesion.getAsientosSeleccionados()) {
                AsientoVendido av = new AsientoVendido();
                av.setFila(as.getFila());
                av.setColumna(as.getColumna());
                av.setPersona(as.getPersona());
                av.setEstado("Vendido");
                av.setVenta(ventaGuardada);
                asientosVendidos.add(av);
            }
            asientoVendidoRepository.saveAll(asientosVendidos);
        }

        estadoSesionService.limpiarEstado();

        return respuestaCatedra;
    }
}
