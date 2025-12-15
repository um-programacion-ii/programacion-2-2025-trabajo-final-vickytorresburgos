package com.mycompany.myapp.evento.infrastructure.persistence.repository;

import com.mycompany.myapp.evento.domain.model.Evento;
import com.mycompany.myapp.evento.domain.ports.out.EventoRepositoryPort;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.evento.infrastructure.persistence.mapper.EventoEntityMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Transactional
public class EventoRepositoryAdapter implements EventoRepositoryPort {

    private final JpaEventoRepository jpaRepository;
    private final EventoEntityMapper mapper;

    public EventoRepositoryAdapter(JpaEventoRepository jpaRepository, EventoEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Evento save(Evento eventoDomain) {
        EventoEntity incomingEntity = mapper.toEntity(eventoDomain);

        Optional<EventoEntity> existingOpt = jpaRepository.findByEventoCatedraId(incomingEntity.getEventoCatedraId());

        EventoEntity entityToSave;

        if (existingOpt.isPresent()) {
            EventoEntity existing = existingOpt.get();

            existing.setTitulo(incomingEntity.getTitulo());
            existing.setResumen(incomingEntity.getResumen());
            existing.setDescripcion(incomingEntity.getDescripcion());
            existing.setFecha(incomingEntity.getFecha());
            existing.setDireccion(incomingEntity.getDireccion());
            existing.setImagen(incomingEntity.getImagen());
            existing.setPrecioEntrada(incomingEntity.getPrecioEntrada());
            existing.setFilaAsientos(incomingEntity.getFilaAsientos());
            existing.setColumnAsientos(incomingEntity.getColumnAsientos());
            existing.setEventoTipoNombre(incomingEntity.getEventoTipoNombre());
            existing.setEventoTipoDescripcion(incomingEntity.getEventoTipoDescripcion());

            existing.getIntegrantes().clear();

            jpaRepository.saveAndFlush(existing);

            if (incomingEntity.getIntegrantes() != null) {
                incomingEntity.getIntegrantes().forEach(i -> {
                    i.setId(null);
                    i.setEvento(existing); // Vinculamos al padre
                    existing.getIntegrantes().add(i);
                });
            }
            entityToSave = existing;
        } else {
            entityToSave = incomingEntity;
            if (entityToSave.getIntegrantes() != null) {
                entityToSave.getIntegrantes().forEach(i -> {
                    i.setId(null);
                    i.setEvento(entityToSave);
                });
            }
        }

        EventoEntity saved = jpaRepository.save(entityToSave);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Evento> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Evento> findByEventoCatedraId(Long idCatedra) {
        return jpaRepository.findByEventoCatedraId(idCatedra).map(mapper::toDomain);
    }

    @Override
    public Page<Evento> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
