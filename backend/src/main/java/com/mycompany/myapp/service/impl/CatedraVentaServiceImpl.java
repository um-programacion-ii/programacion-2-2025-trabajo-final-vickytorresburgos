package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CatedraVentaService;
import com.mycompany.myapp.service.dto.catedra.CatedraVentaDetalleDTO;
import com.mycompany.myapp.service.dto.catedra.CatedraVentaResumidaDTO;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class CatedraVentaServiceImpl implements CatedraVentaService {

    private final Logger log = LoggerFactory.getLogger(CatedraVentaServiceImpl.class);

    private final RestTemplate catedraRestTemplate;

    public CatedraVentaServiceImpl(@Qualifier("catedraRestTemplate") RestTemplate catedraRestTemplate) {
        this.catedraRestTemplate = catedraRestTemplate;
    }

    @Override
    public List<CatedraVentaResumidaDTO> getHistorialVentasCatedra() {
        String urlCatedra = "/api/endpoints/v1/listar-ventas";
        log.debug("Llamando a Cátedra (GET): {}", urlCatedra);

        ResponseEntity<List<CatedraVentaResumidaDTO>> response = catedraRestTemplate.exchange(
            urlCatedra,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<CatedraVentaResumidaDTO>>() {}
        );
        return response.getBody();
    }

    @Override
    public CatedraVentaDetalleDTO getDetalleVentaCatedra(Long ventaId) {
        String urlCatedra = "/api/endpoints/v1/listar-venta/" + ventaId;
        return catedraRestTemplate.getForObject(urlCatedra, CatedraVentaDetalleDTO.class);
    }
}
