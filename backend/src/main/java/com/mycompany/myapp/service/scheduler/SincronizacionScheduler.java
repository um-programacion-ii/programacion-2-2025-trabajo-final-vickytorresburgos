package com.mycompany.myapp.service.scheduler;

import com.mycompany.myapp.service.SincronizacionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SincronizacionScheduler {

    private final Logger log = LoggerFactory.getLogger(SincronizacionScheduler.class);
    private final SincronizacionService sincronizacionService;

    public SincronizacionScheduler(SincronizacionService sincronizacionService) {
        this.sincronizacionService = sincronizacionService;
    }

    /**
     * Se ejecuta automáticamente cada 10 minutos.
     * fixedRate = 600000 ms (10 minutos)
     */
    @Scheduled(fixedRate = 600000)
    public void sincronizarEventosAutomaticamente() {
        log.info("Scheduler: Iniciando sincronización automática de eventos...");
        try {
            sincronizacionService.sincronizarEventos();
            log.info("Scheduler: Sincronización finalizada.");
        } catch (Exception e) {
            log.error("Scheduler: Falló la sincronización automática: {}", e.getMessage());
        }
    }
}
