package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.catedra.CatedraVentaDetalleDTO;
import com.mycompany.myapp.service.dto.catedra.CatedraVentaResumidaDTO;

import java.util.List;

public interface CatedraVentaService {
    /**
     * * Llama a Cátedra para obtener el historial de ventas (resumido)
     */
    List<CatedraVentaResumidaDTO> getHistorialVentasCatedra();

    /**
     * Llama a Cátedra para obtener el detalle de una venta
     */
    CatedraVentaDetalleDTO getDetalleVentaCatedra(Long ventaId);
}
