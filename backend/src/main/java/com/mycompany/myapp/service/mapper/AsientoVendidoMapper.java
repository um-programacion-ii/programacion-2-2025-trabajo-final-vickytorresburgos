package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AsientoVendido;
import com.mycompany.myapp.venta.infrastructure.persistence.entity.VentaEntity;
import com.mycompany.myapp.service.dto.AsientoVendidoDTO;
import com.mycompany.myapp.venta.infrastructure.web.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AsientoVendido} and its DTO {@link AsientoVendidoDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AsientoVendidoMapper extends EntityMapper<AsientoVendidoDTO, AsientoVendido> {
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    AsientoVendidoDTO toDto(AsientoVendido s);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(VentaEntity venta);
}
