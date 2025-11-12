package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Evento;
import com.mycompany.myapp.domain.Integrante;
import com.mycompany.myapp.service.dto.EventoDTO;
import com.mycompany.myapp.service.dto.IntegranteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Integrante} and its DTO {@link IntegranteDTO}.
 */
@Mapper(componentModel = "spring")
public interface IntegranteMapper extends EntityMapper<IntegranteDTO, Integrante> {
    @Mapping(target = "evento", source = "evento", qualifiedByName = "eventoId")
    IntegranteDTO toDto(Integrante s);

    @Named("eventoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventoDTO toDtoEventoId(Evento evento);
}
