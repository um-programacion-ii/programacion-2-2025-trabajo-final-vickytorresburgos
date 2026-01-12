package com.mycompany.myapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign para llamar al Servicio Proxy.
 */
@FeignClient(
    name = "proxyService",
    url = "${feign.client.config.proxyService.url}")
public interface ProxyServiceClient {

    /**
     * Llama al endpoint del Proxy para obtener el JSON del mapa de asientos.
     * El proxy devolverá un JSON que contendrá los asientos Vendidos/Bloqueados.
     */
    @GetMapping("/api/asientos/{eventoId}")
    String getMapaAsientos(@PathVariable("eventoId") Long eventoId);
}
