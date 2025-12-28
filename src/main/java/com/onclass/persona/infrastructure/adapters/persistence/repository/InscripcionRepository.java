package com.onclass.persona.infrastructure.adapters.persistence.repository;

import com.onclass.persona.infrastructure.adapters.persistence.InscripcionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InscripcionRepository extends ReactiveCrudRepository<InscripcionEntity, Long> {
    Flux<InscripcionEntity> findByPersonaId(Long personaId);
    Mono<Long> countByPersonaId(Long personaId);
}
