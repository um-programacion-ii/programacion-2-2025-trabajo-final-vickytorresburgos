package com.mycompany.myapp.venta.infrastructure.persistence.repository;

import com.mycompany.myapp.venta.infrastructure.persistence.entity.VentaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Venta entity.
 */
@Repository
public interface JpaVentaRepository extends JpaRepository<VentaEntity, Long> {
    /**
     * Busca una página de Ventas que pertenecen al usuario logueado.
     * Uso la expresión ?#{authentication.name} de Spring Security
     * para filtrar por el login del usuario actual.
     *
     * @param pageable la información de paginación.
     * @return una página de Ventas.
     */
    @EntityGraph(attributePaths = {"evento", "evento.imagen"})
    @Query("select venta from VentaEntity venta where venta.user.login = ?#{authentication.name}")
    Page<VentaEntity> findByUserIsCurrentUser(Pageable pageable);
}
