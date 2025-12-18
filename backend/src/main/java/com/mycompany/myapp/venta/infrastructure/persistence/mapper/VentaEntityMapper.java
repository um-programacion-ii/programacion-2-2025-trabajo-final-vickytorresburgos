package com.mycompany.myapp.venta.infrastructure.persistence.mapper;

import com.mycompany.myapp.evento.infrastructure.persistence.mapper.EventoEntityMapper;
import com.mycompany.myapp.venta.domain.model.Venta;
import com.mycompany.myapp.venta.infrastructure.persistence.entity.VentaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {EventoEntityMapper.class})
public interface VentaEntityMapper {

    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "evento", target = "evento")
    Venta toDomain(VentaEntity entity);


    @Mapping(target = "user", ignore = true)
    @Mapping(source = "evento", target = "evento")
    VentaEntity toEntity(Venta domain);
}
