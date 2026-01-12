package com.mycompany.myapp.evento.infrastructure.web.mapper;

import com.mycompany.myapp.evento.domain.model.Integrante;
import com.mycompany.myapp.evento.infrastructure.web.dto.IntegranteDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IntegranteDtoMapper {

    @Mapping(target = "evento", ignore = true)
    IntegranteDTO toDto(Integrante domain);

    Integrante toDomain(IntegranteDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Integrante entity, IntegranteDTO dto);
}
