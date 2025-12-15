package com.mycompany.myapp.evento.infrastructure.persistence.repository;

import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Evento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JpaEventoRepository extends JpaRepository<EventoEntity, Long> {
    Optional<EventoEntity> findByEventoCatedraId(Long eventoCatedraId);
}
