package com.mycompany.myapp.venta.infrastructure.persistence.mapper;

import com.mycompany.myapp.venta.domain.model.Venta;
import com.mycompany.myapp.venta.infrastructure.persistence.entity.VentaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VentaEntityMapper {

    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "evento.titulo", target = "eventoTitulo") // Extrae el título
    @Mapping(source = "evento.id", target = "eventoId")
    Venta toDomain(VentaEntity entity);


    @Mapping(target = "user", ignore = true)
    @Mapping(target = "evento", ignore = true)
    VentaEntity toEntity(Venta domain);
}
