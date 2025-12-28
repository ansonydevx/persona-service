package com.onclass.persona.infrastructure.adapters.persistence.repository;

import com.onclass.persona.infrastructure.adapters.persistence.PersonaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PersonaRepository extends ReactiveCrudRepository<PersonaEntity, Long> {

}
