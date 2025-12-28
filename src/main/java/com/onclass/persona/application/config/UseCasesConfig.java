package com.onclass.persona.application.config;

import com.onclass.persona.domain.api.PersonaServicePort;
import com.onclass.persona.domain.spi.BootcampQueryPort;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
import com.onclass.persona.domain.usecase.PersonaUseCase;
import com.onclass.persona.infrastructure.adapters.persistence.PersonaPersistenceAdapter;
import com.onclass.persona.infrastructure.adapters.persistence.repository.InscripcionRepository;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UseCasesConfig {

    private final PersonaRepository personaRepository;
    private final InscripcionRepository inscripcionRepository;

    @Bean
    public PersonaPersistencePort personaPersistencePort() {
        return new PersonaPersistenceAdapter(
                personaRepository, inscripcionRepository);
    }

    @Bean
    public PersonaServicePort personaServicePort(
            PersonaPersistencePort personaPersistencePort,
            BootcampQueryPort bootcampQueryPort
    ) {
        return new PersonaUseCase(personaPersistencePort, bootcampQueryPort);
    }
}
