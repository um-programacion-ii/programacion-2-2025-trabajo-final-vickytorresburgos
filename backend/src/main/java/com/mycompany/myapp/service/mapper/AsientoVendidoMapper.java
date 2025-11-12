package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AsientoVendido;
import com.mycompany.myapp.domain.Venta;
import com.mycompany.myapp.service.dto.AsientoVendidoDTO;
import com.mycompany.myapp.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AsientoVendido} and its DTO {@link AsientoVendidoDTO}.
 */
@Mapper(componentModel = "spring")
public interface AsientoVendidoMapper extends EntityMapper<AsientoVendidoDTO, AsientoVendido> {
    @Mapping(target = "venta", source = "venta", qualifiedByName = "ventaId")
    AsientoVendidoDTO toDto(AsientoVendido s);

    @Named("ventaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    VentaDTO toDtoVentaId(Venta venta);
}
