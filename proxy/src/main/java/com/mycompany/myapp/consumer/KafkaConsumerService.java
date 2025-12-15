package com.mycompany.myapp.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class KafkaConsumerService {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final RestTemplate restTemplate;

    private final String BACKEND_URL = "http://localhost:8080/api/notificaciones/evento-actualizado";

    public KafkaConsumerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Nombre del "topic" de Kafka de la Cátedra.
     * Uso "eventos-catedra" como template.
     */
    private static final String CATEDRA_TOPIC = "${application.kafka.topic}";

    /**
     * consumer group id configurado en application-dev.yml
     */
    private static final String CATEDRA_GROUP_ID = "${spring.kafka.consumer.group-id}";

    /**
     * metodo "listener". Se conecta a Kafka y
     * se queda esperando mensajes en el topic "eventos-catedra".
     */
    @KafkaListener(topics = CATEDRA_TOPIC, groupId = CATEDRA_GROUP_ID)
    public void consume(String message) throws IOException {
        try{
            restTemplate.postForLocation(BACKEND_URL, null);
            log.info("Notificacion enviada el backend correctamente");
        } catch (Exception e) {
            log.error("Error al notificar al backend: {}", e.getMessage());
        }
    }
}
