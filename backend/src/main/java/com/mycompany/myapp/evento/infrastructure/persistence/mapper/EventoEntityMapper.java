package com.mycompany.myapp.evento.infrastructure.persistence.mapper;

import com.mycompany.myapp.evento.domain.model.Evento;
import com.mycompany.myapp.evento.domain.model.Integrante;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.EventoEntity;
import com.mycompany.myapp.evento.infrastructure.persistence.entity.IntegranteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventoEntityMapper {

    Evento toDomain(EventoEntity entity);
    EventoEntity toEntity(Evento domain);

    Integrante toDomain(IntegranteEntity entity);
    IntegranteEntity toEntity(Integrante domain);
}
