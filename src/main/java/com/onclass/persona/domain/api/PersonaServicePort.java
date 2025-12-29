package com.onclass.persona.domain.api;

import com.onclass.persona.domain.model.Persona;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PersonaServicePort {
    Mono<Void> inscribirse(Long personaId, List<Long> bootcampIds);
    Flux<Persona> obtenerPersonasPorBootcamp(Long bootcampId);
}
