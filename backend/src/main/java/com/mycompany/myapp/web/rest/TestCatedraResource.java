package com.mycompany.myapp.web.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/test-catedra")
public class TestCatedraResource {

    private final RestTemplate restTemplate;

    // Inyecta el Bean configurado en el paso 3.1
    public TestCatedraResource(@Qualifier("catedraRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * GET /api/test-catedra/eventos : Prueba la conexión al endpoint de eventos resumidos.
     */
    @GetMapping("/eventos")
    public ResponseEntity<String> testEventosResumidos() {
        // El RestTemplate ya tiene la URL base y el token JWT configurados
        String endpointUrl = "/api/endpoints/v1/eventos-resumidos"; // [cite: 96, 224]

        try {
            String result = restTemplate.getForObject(endpointUrl, String.class);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al conectar con Cátedra: " + e.getMessage());
        }
    }
}
