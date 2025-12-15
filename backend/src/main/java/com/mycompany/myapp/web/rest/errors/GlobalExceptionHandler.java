package com.mycompany.myapp.web.rest.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;

import java.net.ConnectException;
import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja nuestra excepción personalizada (Lógica de Negocio).
     */
    @ExceptionHandler(CatedraException.class)
    public ResponseEntity<ProblemDetail> handleCatedraException(CatedraException ex, WebRequest request) {
        log.warn("Error de negocio con Cátedra: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        problem.setTitle("Error de Cátedra");
        problem.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(ex.getStatus()).body(problem);
    }

    /**
     * Maneja errores de conexión (Servidor caído, VPN desconectada, Timeout).
     */
    @ExceptionHandler({ ResourceAccessException.class, ConnectException.class })
    public ResponseEntity<ProblemDetail> handleConnectionError(Exception ex, WebRequest request) {
        log.error("Fallo de conexión con servicio externo: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.SERVICE_UNAVAILABLE,
            "No se pudo conectar con el servidor de la Cátedra. Verifique su conexión o intente más tarde."
        );
        problem.setTitle("Servicio No Disponible");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problem);
    }

    /**
     * Maneja errores HTTP que devuelve la Cátedra (ej. 401, 404, 500).
     */
    @ExceptionHandler({ HttpClientErrorException.class, HttpServerErrorException.class })
    public ResponseEntity<ProblemDetail> handleHttpError(Exception ex, WebRequest request) {
        log.error("La Cátedra respondió con error: {}", ex.getMessage());

        HttpStatus status = HttpStatus.BAD_GATEWAY;
        String detail = "El servicio de la Cátedra reportó un error.";

        if (ex instanceof HttpClientErrorException clientEx) {
            if (clientEx.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                status = HttpStatus.UNAUTHORIZED;
                detail = "Token de Cátedra inválido o expirado.";
            } else {
                status = HttpStatus.BAD_REQUEST;
                detail = "Datos rechazados por la Cátedra.";
            }
        }

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle("Error en Servicio Externo");
        return ResponseEntity.status(status).body(problem);
    }
}
