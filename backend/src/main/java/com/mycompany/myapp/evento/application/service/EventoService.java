package com.mycompany.myapp.evento.application.service;

import com.mycompany.myapp.evento.domain.model.Evento;
import com.mycompany.myapp.evento.domain.ports.in.GestionarEventoUseCase;
import com.mycompany.myapp.evento.domain.ports.out.EventoRepositoryPort;
import com.mycompany.myapp.evento.infrastructure.web.dto.EventoDTO;
import com.mycompany.myapp.evento.infrastructure.web.mapper.EventoDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class EventoService implements GestionarEventoUseCase {

    private final Logger log = LoggerFactory.getLogger(EventoService.class);

    private final EventoRepositoryPort eventoRepositoryPort;
    private final EventoDtoMapper eventoDtoMapper;

    public EventoService(EventoRepositoryPort eventoRepositoryPort, EventoDtoMapper eventoDtoMapper) {
        this.eventoRepositoryPort = eventoRepositoryPort;
        this.eventoDtoMapper = eventoDtoMapper;
    }

    @Override
    public Evento guardar(Evento evento) {
        return eventoRepositoryPort.save(evento);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> buscarPorId(Long id) {
        return eventoRepositoryPort.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> buscarPorIdCatedra(Long idCatedra) {
        return eventoRepositoryPort.findByEventoCatedraId(idCatedra);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Evento> listarTodos(Pageable pageable) {
        return eventoRepositoryPort.findAll(pageable);
    }

    @Override
    public void borrar(Long id) {
        eventoRepositoryPort.deleteById(id);
    }

    public EventoDTO save(EventoDTO eventoDTO) {
        log.debug("Request to save Evento : {}", eventoDTO);
        Evento domain = eventoDtoMapper.toDomain(eventoDTO);
        domain = eventoRepositoryPort.save(domain);
        return eventoDtoMapper.toDto(domain);
    }

    public EventoDTO update(EventoDTO eventoDTO) {
        log.debug("Request to update Evento : {}", eventoDTO);
        Evento domain = eventoDtoMapper.toDomain(eventoDTO);
        domain = eventoRepositoryPort.save(domain);
        return eventoDtoMapper.toDto(domain);
    }

    public Optional<EventoDTO> partialUpdate(EventoDTO eventoDTO) {
        log.debug("Request to partially update Evento : {}", eventoDTO);
        return eventoRepositoryPort.findById(eventoDTO.getId())
            .map(existingDomain -> {
                eventoDtoMapper.partialUpdate(existingDomain, eventoDTO);
                return existingDomain;
            })
            .map(eventoRepositoryPort::save)
            .map(eventoDtoMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<EventoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Eventos (DTO)");
        return eventoRepositoryPort.findAll(pageable)
            .map(eventoDtoMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<EventoDTO> findOne(Long id) {
        log.debug("Request to get Evento (DTO) : {}", id);
        return eventoRepositoryPort.findById(id)
            .map(eventoDtoMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Evento : {}", id);
        eventoRepositoryPort.deleteById(id);
    }
}
