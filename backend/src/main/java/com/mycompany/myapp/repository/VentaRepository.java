package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Venta entity.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    /**
     * Busca una página de Ventas que pertenecen al usuario logueado.
     * Uso la expresión ?#{authentication.name} de Spring Security
     * para filtrar por el login del usuario actual.
     *
     * @param pageable la información de paginación.
     * @return una página de Ventas.
     */
    @Query("select venta from Venta venta where venta.user.login = ?#{authentication.name}")
    Page<Venta> findByUserIsCurrentUser(Pageable pageable);
}
