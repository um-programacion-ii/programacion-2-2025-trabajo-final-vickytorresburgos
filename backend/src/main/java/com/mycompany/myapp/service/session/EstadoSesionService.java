package com.mycompany.myapp.service.session;
import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EstadoSesionService {

    private final Logger log = LoggerFactory.getLogger(EstadoSesionService.class);
    private static final String KEY_PREFIX = "sesion_usuario:";

    /**
     * Tiempo de expiración de la sesión en Redis.
     */
    private static final Duration SESION_EXPIRATION = Duration.ofMinutes(30);

    private final RedisTemplate<String, EstadoSesionUsuario> sesionRedisTemplate;

    public EstadoSesionService(
        @Qualifier("sesionRedisTemplate") RedisTemplate<String, EstadoSesionUsuario> sesionRedisTemplate
    ) {
        this.sesionRedisTemplate = sesionRedisTemplate;
    }

    /**
     * Guarda el estado de la sesión actual del usuario en Redis.
     *
     * @param estado El objeto de estado a guardar.
     */
    public void guardarEstado(EstadoSesionUsuario estado) {
        Optional<String> loginOpt = getCurrentUserLogin();
        if (loginOpt.isEmpty()) {
            log.warn("No se pudo guardar el estado. Usuario no autenticado.");
            return;
        }

        String login = loginOpt.orElseThrow();
        String redisKey = KEY_PREFIX + login;

        try {
            log.debug("Guardando estado en Redis para {}. Clave: {}", login, redisKey);
            sesionRedisTemplate.opsForValue().set(redisKey, estado, SESION_EXPIRATION);
        } catch (Exception e) {
            log.error("Error al guardar estado en Redis para el usuario {}: {}", login, e.getMessage());
        }
    }

    /**
     * Carga el estado de la sesión del usuario actual desde Redis.
     * Si no se encuentra estado, devuelve un estado nuevo (por defecto).
     *
     * @return El estado de la sesión (guardado o nuevo).
     */
    public EstadoSesionUsuario cargarEstado() {
        Optional<String> loginOpt = getCurrentUserLogin();
        if (loginOpt.isEmpty()) {
            log.warn("No se pudo cargar el estado. Usuario no autenticado. Devolviendo estado vacío.");
            return new EstadoSesionUsuario();
        }

        String login = loginOpt.orElseThrow();
        String redisKey = KEY_PREFIX + login;

        try {
            log.debug("Cargando estado desde Redis para {}. Clave: {}", login, redisKey);
            EstadoSesionUsuario estado = sesionRedisTemplate.opsForValue().get(redisKey);

            if (estado == null) {
                log.debug("No se encontró estado para {}. Creando estado nuevo.", login);
                return new EstadoSesionUsuario();
            }

            sesionRedisTemplate.expire(redisKey, SESION_EXPIRATION);
            return estado;

        } catch (Exception e) {
            log.error("Error al cargar estado de Redis para {}: {}. Devolviendo estado vacío.", login, e.getMessage());
            return new EstadoSesionUsuario();
        }
    }

    /**
     * Limpia (elimina) el estado de la sesión del usuario actual.
     * Se usa al cerrar sesión.
     */
    public void limpiarEstado() {
        Optional<String> loginOpt = getCurrentUserLogin();
        if (loginOpt.isEmpty()) {
            return;
        }

        String redisKey = KEY_PREFIX + loginOpt.orElseThrow();
        log.debug("Limpiando estado en Redis para {}.", loginOpt.orElseThrow());
        sesionRedisTemplate.delete(redisKey);
    }

    /**
     * Metodo helper para obtener el login del usuario autenticado.
     */
    private Optional<String> getCurrentUserLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            return Optional.empty();
        }
        return Optional.of(authentication.getName());
    }
}
