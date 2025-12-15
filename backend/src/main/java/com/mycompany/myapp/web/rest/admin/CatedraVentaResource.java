package com.mycompany.myapp.web.rest.admin;

import com.mycompany.myapp.service.CatedraVentaService;
import com.mycompany.myapp.service.dto.catedra.CatedraVentaDetalleDTO;
import com.mycompany.myapp.service.dto.catedra.CatedraVentaResumidaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class CatedraVentaResource {

    private final Logger log = LoggerFactory.getLogger(CatedraVentaResource.class);
    private final CatedraVentaService catedraVentaService;

    public CatedraVentaResource(CatedraVentaService catedraVentaService) {
        this.catedraVentaService = catedraVentaService;
    }

    /**
     * {@code GET /catedra-ventas} : Obtiene el historial de ventas desde Cátedra.
     */
    @GetMapping("/catedra-ventas")
    public ResponseEntity<List<CatedraVentaResumidaDTO>> getHistorialCatedra() {
        List<CatedraVentaResumidaDTO> historial = catedraVentaService.getHistorialVentasCatedra();
        return ResponseEntity.ok(historial);
    }

    /**
     * {@code GET /catedra-ventas/:id} : Obtiene el detalle de una venta desde Cátedra.
     */
    @GetMapping("/catedra-ventas/{id}")
    public ResponseEntity<CatedraVentaDetalleDTO> getDetalleCatedra(@PathVariable Long id) {
        CatedraVentaDetalleDTO detalle = catedraVentaService.getDetalleVentaCatedra(id);
        return ResponseEntity.ok(detalle);
    }
}
