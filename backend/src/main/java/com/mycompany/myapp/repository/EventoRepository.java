package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Evento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Evento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    Optional<Evento> findByEventoCatedraId(Long eventoCatedraId);
}
