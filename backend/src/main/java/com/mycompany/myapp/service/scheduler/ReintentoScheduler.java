package com.mycompany.myapp.service.scheduler;

import com.mycompany.myapp.venta.infrastructure.persistence.entity.VentaEntity;
import com.mycompany.myapp.venta.infrastructure.persistence.repository.JpaVentaRepository;
import com.mycompany.myapp.service.CatedraVentaService;
import com.mycompany.myapp.service.dto.catedra.CatedraVentaResumidaDTO;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReintentoScheduler {
    private final Logger log = LoggerFactory.getLogger(ReintentoScheduler.class);
    private final JpaVentaRepository ventaRepository;
    private final CatedraVentaService catedraVentaService;

    public ReintentoScheduler(JpaVentaRepository ventaRepository, CatedraVentaService catedraVentaService) {
        this.ventaRepository = ventaRepository;
        this.catedraVentaService = catedraVentaService;
    }

    /**
     * Busca ventas locales en estado PENDIENTE (resultado = null)
     * e intenta averiguar su estado real consultando a la Cátedra.
     * Se ejecuta cada hora.
     */
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void reconciliarVentasPendientes() {
        log.info("Iniciando reintento de ventas pendientes...");

        // Busca ventas locales donde 'resultado' es NULL
        List<VentaEntity> ventasPendientes = ventaRepository.findAll().stream()
            .filter(v -> v.getResultado() == null)
            .toList();

        if (ventasPendientes.isEmpty()) {
            log.info("No hay ventas pendientes para reintentar.");
            return;
        }

        log.info("Se encontraron {} ventas pendientes localmente.", ventasPendientes.size());

        try {
            List<CatedraVentaResumidaDTO> historialCatedra = catedraVentaService.getHistorialVentasCatedra();

            for (VentaEntity ventaLocal : ventasPendientes) {

                boolean encontrada = historialCatedra.stream().anyMatch(vCatedra ->
                    vCatedra.getEventoId().equals(ventaLocal.getEvento().getEventoCatedraId()) &&
                        vCatedra.getPrecioVenta().compareTo(ventaLocal.getPrecioVenta()) == 0 &&
                        vCatedra.isResultado()
                );

                if (encontrada) {
                    log.info("Venta Local {} confirmada en Cátedra. Actualizando...", ventaLocal.getId());
                    ventaLocal.setResultado(true);
                    ventaLocal.setDescripcion("Confirmada por proceso de reintento");
                    ventaRepository.save(ventaLocal);
                } else {
                    log.warn("Venta Local {} no encontrada en Cátedra. Sigue pendiente o falló.", ventaLocal.getId());
                }
            }

        } catch (Exception e) {
            log.error("Error al reintentar: {}", e.getMessage());
        }
    }
}

