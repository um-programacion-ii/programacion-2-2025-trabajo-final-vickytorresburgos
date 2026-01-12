package com.mycompany.myapp.service;

import com.mycompany.myapp.client.ProxyServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AsientoMapaService {

    private final Logger log = LoggerFactory.getLogger(AsientoMapaService.class);
    private final ProxyServiceClient proxyServiceClient;

    public AsientoMapaService(ProxyServiceClient proxyServiceClient) {
        this.proxyServiceClient = proxyServiceClient;
    }

    /**
     * Obtiene el mapa de asientos del evento llamando al Proxy.
     */
    public String obtenerMapaAsientosDeCatedra(Long eventoId) {
        try {
            String mapaAsientosJson = proxyServiceClient.getMapaAsientos(eventoId);
            log.debug("Mapa de asientos real recibido del Proxy para evento {}: {}", eventoId, mapaAsientosJson);
            return mapaAsientosJson;
        } catch (Exception e) {
            return "{\"eventoId\": " + eventoId + ", \"asientos\": [], \"error\": \"Proxy no disponible\"}";
        }
    }
}
