package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.enums.TechnicalMessage;
import com.onclass.persona.domain.exceptions.BusinessException;
import com.onclass.persona.domain.spi.BootcampQueryPort;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
import com.onclass.persona.domain.spi.ReporteCommandPort;
import com.onclass.persona.infrastructure.entrypoints.dto.BootcampResumen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

class PersonaUseCaseTest {

    private PersonaPersistencePort persistencePort;
    private BootcampQueryPort bootcampQueryPort;
    private ReporteCommandPort reporteCommandPort;
    private PersonaUseCase useCase;

    @BeforeEach
    void setup() {
        persistencePort = Mockito.mock(PersonaPersistencePort.class);
        bootcampQueryPort = Mockito.mock(BootcampQueryPort.class);
        reporteCommandPort = Mockito.mock(ReporteCommandPort.class);

        useCase = new PersonaUseCase(persistencePort, bootcampQueryPort, reporteCommandPort);

        when(persistencePort.saveInscripciones(anyLong(), anyList()))
                .thenReturn(Mono.empty());
        when(reporteCommandPort.incrementarPersonas(anyList()))
                .thenReturn(Mono.empty());
    }

    @Test
    void deberiaInscribirseCuandoNoHayCruce() {
        Long personaId = 1L;
        List<Long> nuevos = List.of(10L);

        when(persistencePort.existsPersonaById(personaId))
                .thenReturn(Mono.just(true));
        when(persistencePort.countInscripciones(personaId))
                .thenReturn(Mono.just(1L));
        when(persistencePort.findBootcampIdsByPersona(personaId))
                .thenReturn(Flux.just(1L));

        BootcampResumen actual = new BootcampResumen(
                1L,
                LocalDate.of(2025, 1, 1),
                4
        );

        BootcampResumen nuevo = new BootcampResumen(
                10L,
                LocalDate.of(2025, 2, 1),
                4
        );

        when(bootcampQueryPort.obtenerBootcampsPorIds(List.of(1L)))
                .thenReturn(Flux.just(actual));
        when(bootcampQueryPort.obtenerBootcampsPorIds(nuevos))
                .thenReturn(Flux.just(nuevo));

        when(persistencePort.saveInscripciones(personaId, nuevos))
                .thenReturn(Mono.empty());

        when(reporteCommandPort.incrementarPersonas(nuevos))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.inscribirse(personaId, nuevos))
                .verifyComplete();

        verify(persistencePort).saveInscripciones(personaId, nuevos);
        verify(reporteCommandPort).incrementarPersonas(nuevos);
    }

    @Test
    void deberiaFallarCuandoHayCruceDeBootcamps() {
        Long personaId = 1L;
        List<Long> nuevos = List.of(10L);

        when(persistencePort.existsPersonaById(personaId))
                .thenReturn(Mono.just(true));
        when(persistencePort.countInscripciones(personaId))
                .thenReturn(Mono.just(1L));
        when(persistencePort.findBootcampIdsByPersona(personaId))
                .thenReturn(Flux.just(1L));

        BootcampResumen actual = new BootcampResumen(
                1L,
                LocalDate.of(2025, 3, 1),
                4
        );

        BootcampResumen nuevo = new BootcampResumen(
                10L,
                LocalDate.of(2025, 3, 28),
                4
        );

        when(bootcampQueryPort.obtenerBootcampsPorIds(List.of(1L)))
                .thenReturn(Flux.just(actual));
        when(bootcampQueryPort.obtenerBootcampsPorIds(nuevos))
                .thenReturn(Flux.just(nuevo));

        StepVerifier.create(useCase.inscribirse(personaId, nuevos))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getTechnicalMessage()
                                        .equals(TechnicalMessage.CRUCE_BOOTCAMPS)
                )
                .verify();
    }

    @Test
    void deberiaFallarSiSeEnvianMasDe5Bootcamps() {
        Long personaId = 1L;
        List<Long> bootcamps = List.of(1L,2L,3L,4L,5L,6L);

        when(persistencePort.existsPersonaById(personaId))
                .thenReturn(Mono.just(true));

        when(persistencePort.countInscripciones(personaId))
                .thenReturn(Mono.just(0L));

        StepVerifier.create(useCase.inscribirse(personaId, bootcamps))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getTechnicalMessage()
                                        .equals(TechnicalMessage.MAXIMO_BOOTCAMPS)
                )
                .verify();
    }

    @Test
    void deberiaFallarCuandoExcedeMaximoPermitido() {
        Long personaId = 1L;
        List<Long> nuevos = List.of(10L, 11L);

        when(persistencePort.existsPersonaById(personaId))
                .thenReturn(Mono.just(true));
        when(persistencePort.countInscripciones(personaId))
                .thenReturn(Mono.just(4L));

        StepVerifier.create(useCase.inscribirse(personaId, nuevos))
                .expectErrorMatches(e ->
                        e instanceof BusinessException &&
                                ((BusinessException) e).getTechnicalMessage()
                                        .equals(TechnicalMessage.MAXIMO_BOOTCAMPS)
                )
                .verify();
    }
}
