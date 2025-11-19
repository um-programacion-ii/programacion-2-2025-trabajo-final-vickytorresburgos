package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Evento;
import com.mycompany.myapp.domain.Integrante;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Integrante entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegranteRepository extends JpaRepository<Integrante, Long> {
    void deleteByEvento(Evento evento);
}
