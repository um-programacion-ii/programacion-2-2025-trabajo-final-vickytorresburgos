package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.catedra.BloquearAsientosResponse;
import com.mycompany.myapp.service.dto.catedra.RealizarVentaDTO;

/**
 * Service Interface for managing the purchase flow logic.
 */
public interface FlujoCompraService {

    /**
     * Ejecuta la lógica de bloqueo de asientos contra la Cátedra.
     * Valida el estado de la sesión y actualiza el paso.
     *
     * @return El estado de sesión actualizado.
     */
    BloquearAsientosResponse bloquearAsientos();

    /**
     * Ejecuta la lógica de venta final.
     * Llama a Cátedra, persiste la venta localmente y limpia la sesión.
     *
     * @return La respuesta de la venta de Cátedra.
     */
    RealizarVentaDTO realizarVenta();
}
