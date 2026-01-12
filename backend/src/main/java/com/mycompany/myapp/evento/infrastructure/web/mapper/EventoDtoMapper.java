package com.mycompany.myapp.evento.infrastructure.web.mapper;

import com.mycompany.myapp.evento.domain.model.Evento;
import com.mycompany.myapp.evento.domain.model.Integrante;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.evento.infrastructure.web.dto.EventoDTO;
import com.mycompany.myapp.evento.infrastructure.web.dto.IntegranteDTO;
import com.mycompany.myapp.service.mapper.EntityMapper;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventoDtoMapper {
    Evento toDomain(EventoDTO dto);

    EventoDTO toDto(Evento domain);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Evento entity, EventoDTO dto);

    Integrante toDomain(IntegranteDTO dto);
    IntegranteDTO toDto(Integrante domain);
}
