package com.onclass.persona.domain.api;

import reactor.core.publisher.Mono;

import java.util.List;

public interface PersonaServicePort {
    Mono<Void> inscribirse(Long personaId, List<Long> bootcampIds);
}
