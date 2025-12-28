package com.onclass.persona.domain.spi;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PersonaPersistencePort {
    Mono<Boolean> existsPersonaById(Long personaId);
    Flux<Long> findBootcampIdsByPersona(Long personaId);
    Mono<Long> countInscripciones(Long personaId);
    Mono<Void> saveInscripciones(Long personaId, List<Long> bootcampIds);
}
