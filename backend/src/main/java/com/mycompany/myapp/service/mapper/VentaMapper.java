package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Evento;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.Venta;
import com.mycompany.myapp.service.dto.EventoDTO;
import com.mycompany.myapp.service.dto.UserDTO;
import com.mycompany.myapp.service.dto.VentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Venta} and its DTO {@link VentaDTO}.
 */
@Mapper(componentModel = "spring")
public interface VentaMapper extends EntityMapper<VentaDTO, Venta> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "evento", source = "evento", qualifiedByName = "eventoId")
    VentaDTO toDto(Venta s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("eventoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventoDTO toDtoEventoId(Evento evento);
}
