package com.mycompany.myapp.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.service.session.EstadoSesionUsuario;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class SesionRedisConfiguration {
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean("sesionConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(config);
    }

    /**
     * Define un 'conector' (RedisTemplate) específico para la
     * clase EstadoSesionUsuario.
     *
     * @param connectionFactory La conexión a Redis (gestionada por Spring).
     * @param objectMapper    JHipster ya provee un ObjectMapper para JSON.
     * @return Un RedisTemplate configurado.
     */
    @Bean("sesionRedisTemplate")
    public RedisTemplate<String, EstadoSesionUsuario> sesionRedisTemplate(
        @Qualifier("sesionConnectionFactory") RedisConnectionFactory connectionFactory,
        ObjectMapper objectMapper
    ) {
        RedisTemplate<String, EstadoSesionUsuario> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<EstadoSesionUsuario> jsonSerializer = new Jackson2JsonRedisSerializer<>(
            objectMapper,
            EstadoSesionUsuario.class
        );
        template.setValueSerializer(jsonSerializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
