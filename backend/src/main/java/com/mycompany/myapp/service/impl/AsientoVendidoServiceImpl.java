package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.AsientoVendido;
import com.mycompany.myapp.repository.AsientoVendidoRepository;
import com.mycompany.myapp.service.AsientoVendidoService;
import com.mycompany.myapp.service.dto.AsientoVendidoDTO;
import com.mycompany.myapp.service.mapper.AsientoVendidoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AsientoVendido}.
 */
@Service
@Transactional
public class AsientoVendidoServiceImpl implements AsientoVendidoService {

    private static final Logger LOG = LoggerFactory.getLogger(AsientoVendidoServiceImpl.class);

    private final AsientoVendidoRepository asientoVendidoRepository;

    private final AsientoVendidoMapper asientoVendidoMapper;

    public AsientoVendidoServiceImpl(AsientoVendidoRepository asientoVendidoRepository, AsientoVendidoMapper asientoVendidoMapper) {
        this.asientoVendidoRepository = asientoVendidoRepository;
        this.asientoVendidoMapper = asientoVendidoMapper;
    }

    @Override
    public AsientoVendidoDTO save(AsientoVendidoDTO asientoVendidoDTO) {
        LOG.debug("Request to save AsientoVendido : {}", asientoVendidoDTO);
        AsientoVendido asientoVendido = asientoVendidoMapper.toEntity(asientoVendidoDTO);
        asientoVendido = asientoVendidoRepository.save(asientoVendido);
        return asientoVendidoMapper.toDto(asientoVendido);
    }

    @Override
    public AsientoVendidoDTO update(AsientoVendidoDTO asientoVendidoDTO) {
        LOG.debug("Request to update AsientoVendido : {}", asientoVendidoDTO);
        AsientoVendido asientoVendido = asientoVendidoMapper.toEntity(asientoVendidoDTO);
        asientoVendido = asientoVendidoRepository.save(asientoVendido);
        return asientoVendidoMapper.toDto(asientoVendido);
    }

    @Override
    public Optional<AsientoVendidoDTO> partialUpdate(AsientoVendidoDTO asientoVendidoDTO) {
        LOG.debug("Request to partially update AsientoVendido : {}", asientoVendidoDTO);

        return asientoVendidoRepository
            .findById(asientoVendidoDTO.getId())
            .map(existingAsientoVendido -> {
                asientoVendidoMapper.partialUpdate(existingAsientoVendido, asientoVendidoDTO);

                return existingAsientoVendido;
            })
            .map(asientoVendidoRepository::save)
            .map(asientoVendidoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsientoVendidoDTO> findAll() {
        LOG.debug("Request to get all AsientoVendidos");
        return asientoVendidoRepository
            .findAll()
            .stream()
            .map(asientoVendidoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AsientoVendidoDTO> findOne(Long id) {
        LOG.debug("Request to get AsientoVendido : {}", id);
        return asientoVendidoRepository.findById(id).map(asientoVendidoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AsientoVendido : {}", id);
        asientoVendidoRepository.deleteById(id);
    }
}
