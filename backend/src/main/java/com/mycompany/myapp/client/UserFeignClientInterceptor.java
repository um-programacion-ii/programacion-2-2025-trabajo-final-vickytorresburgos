package com.mycompany.myapp.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class UserFeignClientInterceptor implements RequestInterceptor {

    private final Logger log = LoggerFactory.getLogger(UserFeignClientInterceptor.class);
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate template) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            if (authentication instanceof JwtAuthenticationToken jwtToken) {
                String tokenValue = jwtToken.getToken().getTokenValue();
                log.debug("Token JWT encontrado (JwtAuthenticationToken), inyectando en header.");
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, tokenValue));
            } else if (authentication.getCredentials() instanceof String tokenValue) {
                log.debug("Token JWT encontrado (Credentials), inyectando en header.");
                template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE, tokenValue));
            } else {
                log.warn("Usuario autenticado pero no se pudo extraer el token JWT. Tipo: {}", authentication.getClass().getName());
            }
        } else {
            log.warn("No hay autenticación en el contexto. La petición al Proxy saldrá anónima.");
        }
    }
}
