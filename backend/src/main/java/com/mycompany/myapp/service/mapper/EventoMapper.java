package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Evento;
import com.mycompany.myapp.service.dto.EventoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Evento} and its DTO {@link EventoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventoMapper extends EntityMapper<EventoDTO, Evento> {}
