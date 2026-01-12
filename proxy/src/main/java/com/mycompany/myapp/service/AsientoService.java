package com.mycompany.myapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class AsientoService {

    private final Logger log = LoggerFactory.getLogger(AsientoService.class);

    private final StringRedisTemplate redisTemplate;

    public AsientoService(@Qualifier("catedraRedisTemplate") StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Obtiene el mapa de asientos desde el Redis de Cátedra.
     *
     * @param eventoId El ID del evento a consultar.
     * @return Un String (que se espera sea JSON) con el estado de los asientos.
     */
    public String getMapaAsientos(Long eventoId) {
        String redisKey = String.format("evento_%d", eventoId);

        log.debug("Consultando Redis de Cátedra. Clave: {}", redisKey);

        try {
            String mapaAsientosJson = redisTemplate.opsForValue().get(redisKey);

            if (mapaAsientosJson == null) {
                log.warn("No se encontró mapa de asientos para la clave: {}", redisKey);
                return "{\"eventoId\": " + eventoId + ", \"asientos\": []}";
            }

            return mapaAsientosJson;
        } catch (Exception e) {
            log.error("Error al leer de Redis Cátedra (Clave: {}): {}", redisKey, e.getMessage());
            return "{\"error\": \"No se pudo cargar el mapa de asientos\"}";
        }
    }
}
