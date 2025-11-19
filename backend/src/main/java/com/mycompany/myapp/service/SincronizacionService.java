package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Evento;
import com.mycompany.myapp.domain.Integrante;
import com.mycompany.myapp.repository.EventoRepository;
import com.mycompany.myapp.repository.IntegranteRepository;
import com.mycompany.myapp.service.dto.catedra.EventoCatedraDTO;
import com.mycompany.myapp.service.dto.catedra.IntegranteDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
public class SincronizacionService {

    private final Logger log = LoggerFactory.getLogger(SincronizacionService.class);

    private final RestTemplate catedraRestTemplate;
    private final EventoRepository eventoRepository;
    private final IntegranteRepository integranteRepository;

    public SincronizacionService(
        @Qualifier("catedraRestTemplate") RestTemplate catedraRestTemplate,
        EventoRepository eventoRepository,
        IntegranteRepository integranteRepository
    ) {
        this.catedraRestTemplate = catedraRestTemplate;
        this.eventoRepository = eventoRepository;
        this.integranteRepository = integranteRepository;
    }

    /**
     * Llama a la API de Cátedra, obtiene todos los eventos
     * y los guarda/actualiza en la base de datos local.
     */
    public void sincronizarEventos() {
        log.info("Iniciando sincronización de eventos desde Cátedra...");

        String urlCatedra = "/api/endpoints/v1/eventos";

        ResponseEntity<List<EventoCatedraDTO>> response;
        try {
            response =
                catedraRestTemplate.exchange(
                    urlCatedra,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<EventoCatedraDTO>>() {}
                );
        } catch (Exception e) {
            log.error("Error al llamar a /api/endpoints/v1/eventos: {}", e.getMessage());
            return;
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("La Cátedra devolvió un error: {}", response.getStatusCode());
            return;
        }

        List<EventoCatedraDTO> eventosCatedra = response.getBody();
        log.debug("Se recibieron {} eventos de la Cátedra", eventosCatedra.size());

        for (EventoCatedraDTO dto : eventosCatedra) {
            Evento evento = eventoRepository
                .findByEventoCatedraId(dto.getEventoCatedraId())
                .orElse(new Evento()); // Si no existe, crea uno nuevo

            evento.setEventoCatedraId(dto.getEventoCatedraId());
            evento.setTitulo(dto.getTitulo());
            evento.setDescripcion(dto.getDescripcion());
            evento.setFecha(dto.getFecha());
            evento.setFilaAsientos(dto.getFilaAsientos());
            evento.setColumnAsientos(dto.getColumnAsientos());
            evento.setPrecioEntrada(dto.getPrecioEntrada());

            if (dto.getEventoTipo() != null) {
                evento.setEventoTipoNombre(dto.getEventoTipo().getNombre());
                evento.setEventoTipoDescripcion(dto.getEventoTipo().getDescripcion());
            }

            Evento eventoGuardado = eventoRepository.save(evento);

            integranteRepository.deleteByEvento(eventoGuardado);

            Set<Integrante> integrantesNuevos = new HashSet<>();
            if (dto.getIntegrantes() != null) {
                for (IntegranteDTO integranteDTO : dto.getIntegrantes()) {
                    Integrante integrante = new Integrante();
                    integrante.setNombre(integranteDTO.getNombre());
                    integrante.setApellido(integranteDTO.getApellido());
                    integrante.setIdentificacion(integranteDTO.getIdentificacion());
                    integrante.setEvento(eventoGuardado);
                    integrantesNuevos.add(integrante);
                }
            }

            if (!integrantesNuevos.isEmpty()) {
                integranteRepository.saveAll(integrantesNuevos);
            }
        }
        log.info("Sincronización de eventos completada. Total: {}", eventosCatedra.size());
    }
}
