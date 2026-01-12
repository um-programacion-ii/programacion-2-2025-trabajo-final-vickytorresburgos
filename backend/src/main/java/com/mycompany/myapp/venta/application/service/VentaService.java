package com.mycompany.myapp.venta.application.service;

import com.mycompany.myapp.venta.domain.model.Venta;
import com.mycompany.myapp.venta.domain.ports.in.RealizarVentaUseCase;
import com.mycompany.myapp.venta.domain.ports.out.VentaRepositoryPort;
import com.mycompany.myapp.venta.infrastructure.web.dto.VentaDTO;
import com.mycompany.myapp.venta.infrastructure.web.mapper.VentaDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VentaService implements RealizarVentaUseCase {

    private final Logger log = LoggerFactory.getLogger(VentaService.class);
    private final VentaRepositoryPort ventaRepositoryPort;
    private final VentaDtoMapper ventaDtoMapper;

    public VentaService(VentaRepositoryPort ventaRepositoryPort, VentaDtoMapper ventaDtoMapper) {
        this.ventaRepositoryPort = ventaRepositoryPort;
        this.ventaDtoMapper = ventaDtoMapper;
    }

    @Override
    public Venta realizarVenta(Venta venta) {
        venta.confirmar();
        return ventaRepositoryPort.save(venta);
    }

    public VentaDTO save(VentaDTO ventaDTO) {
        log.debug("Request to save Venta : {}", ventaDTO);
        Venta domain = ventaDtoMapper.toDomain(ventaDTO);
        domain = ventaRepositoryPort.save(domain);
        return ventaDtoMapper.toDto(domain);
    }

    public VentaDTO update(VentaDTO ventaDTO) {
        log.debug("Request to update Venta : {}", ventaDTO);
        Venta domain = ventaDtoMapper.toDomain(ventaDTO);
        domain = ventaRepositoryPort.save(domain);
        return ventaDtoMapper.toDto(domain);
    }

    public Optional<VentaDTO> partialUpdate(VentaDTO ventaDTO) {
        log.debug("Request to partially update Venta : {}", ventaDTO);

        return ventaRepositoryPort
            .findById(ventaDTO.getId())
            .map(existingDomain -> {
                ventaDtoMapper.partialUpdate(existingDomain, ventaDTO);
                return existingDomain;
            })
            .map(ventaRepositoryPort::save)
            .map(ventaDtoMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<VentaDTO> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepositoryPort.findById(id)
            .map(ventaDtoMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        ventaRepositoryPort.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<VentaDTO> findByUserIsCurrentUser(Pageable pageable) {
        log.debug("Request to get all Ventas for current user");
        return ventaRepositoryPort.findByUserIsCurrentUser(pageable)
            .map(ventaDtoMapper::toDto);
    }
}
