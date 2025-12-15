package com.mycompany.myapp.venta.infrastructure.persistence.repository;

import com.mycompany.myapp.venta.domain.model.Venta;
import com.mycompany.myapp.venta.domain.ports.out.VentaRepositoryPort;
import com.mycompany.myapp.venta.infrastructure.persistence.mapper.VentaEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaVentaRepositoryAdapter implements VentaRepositoryPort {

    private final JpaVentaRepository jpaRepository;
    private final VentaEntityMapper mapper;

    public JpaVentaRepositoryAdapter(JpaVentaRepository jpaRepository, VentaEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Venta save(Venta venta) {
        var entity = mapper.toEntity(venta);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Venta> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Page<Venta> findByUserIsCurrentUser(Pageable pageable) {
        return jpaRepository.findByUserIsCurrentUser(pageable)
            .map(mapper::toDomain);
    }
}
