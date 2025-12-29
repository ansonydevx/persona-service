package com.onclass.persona.infrastructure.adapters.persistence;

import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
import com.onclass.persona.infrastructure.adapters.persistence.repository.InscripcionRepository;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PersonaPersistenceAdapter implements PersonaPersistencePort {

    private final PersonaRepository personaRepository;
    private final InscripcionRepository inscripcionRepository;


    @Override
    public Mono<Boolean> existsPersonaById(Long personaId) {
        return personaRepository.existsById(personaId);
    }

    @Override
    public Flux<Long> findBootcampIdsByPersona(Long personaId) {
        return inscripcionRepository.findByPersonaId(personaId)
                .map(InscripcionEntity::getBootcampId);
    }

    @Override
    public Mono<Long> countInscripciones(Long personaId) {
        return inscripcionRepository.countByPersonaId(personaId);
    }

    @Override
    public Mono<Void> saveInscripciones(Long personaId, List<Long> bootcampIds) {
        return Flux.fromIterable(bootcampIds)
                .map(bootcampId -> new InscripcionEntity(personaId, bootcampId))
                .flatMap(inscripcionRepository::save)
                .then();
    }

    @Override
    public Flux<Persona> findPersonasByBootcampId(Long bootcampId) {
        return inscripcionRepository.findByBootcampId(bootcampId)
                .map(InscripcionEntity::getPersonaId)
                .flatMap(personaRepository::findById)
                .map(entity ->
                        new Persona(
                                entity.getId(),
                                entity.getNombre(),
                                entity.getEmail()
                        ));
    }
}
