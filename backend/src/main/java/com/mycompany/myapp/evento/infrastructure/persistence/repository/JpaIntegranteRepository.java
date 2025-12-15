package com.mycompany.myapp.evento.infrastructure.persistence.repository;

import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.IntegranteEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Integrante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JpaIntegranteRepository extends JpaRepository<IntegranteEntity, Long> {
    void deleteByEvento(EventoEntity evento);
}
