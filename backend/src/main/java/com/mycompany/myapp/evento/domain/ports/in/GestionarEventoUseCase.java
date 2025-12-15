package com.mycompany.myapp.evento.domain.ports.in;

import com.mycompany.myapp.evento.domain.model.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface GestionarEventoUseCase {
    Evento guardar(Evento evento);
    Optional<Evento> buscarPorId(Long id);
    Optional<Evento> buscarPorIdCatedra(Long idCatedra);
    Page<Evento> listarTodos(Pageable pageable);
    void borrar(Long id);
}
