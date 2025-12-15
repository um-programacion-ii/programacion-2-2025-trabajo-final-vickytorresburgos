package com.mycompany.myapp.service;

import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.IntegranteEntity;
import com.mycompany.myapp.evento.infrastructure.persistence.repository.JpaEventoRepository;
import com.mycompany.myapp.evento.infrastructure.persistence.repository.JpaIntegranteRepository;
import com.mycompany.myapp.service.dto.catedra.EventoCatedraDTO;
import com.mycompany.myapp.service.dto.catedra.IntegranteDTO;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
    private final JpaEventoRepository eventoRepository;
    private final JpaIntegranteRepository integranteRepository;

    @Value("${application.catedra.jwt-token}")
    private String catedraToken;

    @Value("${application.catedra.base-url}")
    private String catedraUrl;

    public SincronizacionService(
        @Qualifier("catedraRestTemplate") RestTemplate catedraRestTemplate,
        JpaEventoRepository eventoRepository,
        JpaIntegranteRepository integranteRepository
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
        String endpoint = "/api/endpoints/v1/eventos";
        String fullUrl = catedraUrl + endpoint;

        ResponseEntity<List<EventoCatedraDTO>> response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(catedraToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            response =
                catedraRestTemplate.exchange(
                    fullUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<EventoCatedraDTO>>() {}
                );
        } catch (Exception e) {
            log.error("Error al llamar a {}: {}", fullUrl, e.getMessage());
            return;
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.error("La Cátedra devolvió un error: {}", response.getStatusCode());
            return;
        }

        List<EventoCatedraDTO> eventosCatedra = response.getBody();
        log.debug("Se recibieron {} eventos de la Cátedra", eventosCatedra.size());

        for (EventoCatedraDTO dto : eventosCatedra) {
            EventoEntity evento = eventoRepository
                .findByEventoCatedraId(dto.getEventoCatedraId())
                .orElse(new EventoEntity()); // Si no existe, crea uno nuevo

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

            EventoEntity eventoGuardado = eventoRepository.save(evento);

            // Actualizar integrantes
            integranteRepository.deleteByEvento(eventoGuardado);

            Set<IntegranteEntity> integrantesNuevos = new HashSet<>();
            if (dto.getIntegrantes() != null) {
                for (IntegranteDTO integranteDTO : dto.getIntegrantes()) {
                    IntegranteEntity integrante = new IntegranteEntity();
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
