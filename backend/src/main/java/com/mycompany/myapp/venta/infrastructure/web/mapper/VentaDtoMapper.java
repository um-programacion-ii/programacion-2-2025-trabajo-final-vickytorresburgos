package com.mycompany.myapp.venta.infrastructure.web.mapper;

import com.mycompany.myapp.venta.domain.model.Venta;
import com.mycompany.myapp.venta.infrastructure.web.dto.VentaDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VentaDtoMapper {
    Venta toDomain(VentaDTO dto);

    @Mapping(source = "userLogin", target = "user.login")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "eventoTitulo", target = "evento.titulo")
    @Mapping(source = "eventoId", target = "evento.id")
    VentaDTO toDto(Venta domain);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Venta entity, VentaDTO dto);
}
