package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.enums.TechnicalMessage;
import com.onclass.persona.domain.exceptions.BusinessException;
import com.onclass.persona.domain.api.PersonaServicePort;
import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.domain.spi.BootcampQueryPort;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
import com.onclass.persona.domain.spi.ReporteCommandPort;
import com.onclass.persona.infrastructure.entrypoints.dto.BootcampResumen;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;

@Slf4j
public class PersonaUseCase implements PersonaServicePort {

    private final PersonaPersistencePort persistencePort;
    private final BootcampQueryPort bootcampQueryPort;
    private final ReporteCommandPort reporteCommandPort;

    private static final int MAXIMO_BOOTCAMPS = 5;

    public PersonaUseCase(
            PersonaPersistencePort persistencePort,
            BootcampQueryPort bootcampQueryPort,
            ReporteCommandPort reporteCommandPort
    ) {
        this.persistencePort = persistencePort;
        this.bootcampQueryPort = bootcampQueryPort;
        this.reporteCommandPort = reporteCommandPort;
    }

    @Override
    public Mono<Void> inscribirse(Long personaId, List<Long> bootcampIds) {
        if (bootcampIds == null || bootcampIds.isEmpty()) {
            return Mono.empty();
        }

        return validarPersonaExiste(personaId)
                .then(validarInscripcion(personaId, bootcampIds))
                .then(persistencePort.saveInscripciones(personaId, bootcampIds)
                        .then(reporteCommandPort.incrementarPersonas(bootcampIds))
                );
    }

    @Override
    public Flux<Persona> obtenerPersonasPorBootcamp(Long bootcampId) {
        return persistencePort.findPersonasByBootcampId(bootcampId);
    }

    private Mono<Void> validarInscripcion(Long personaId, List<Long> nuevosBootcampIds) {
        return persistencePort.countInscripciones(personaId)
                .flatMap(count -> {
                    if (count + nuevosBootcampIds.size() > MAXIMO_BOOTCAMPS) {
                        return Mono.error(new BusinessException(TechnicalMessage.MAXIMO_BOOTCAMPS));
                    }
                    return validarCruceFechas(personaId, nuevosBootcampIds);
                });
    }

    private Mono<Void> validarPersonaExiste(Long personaId) {
        return persistencePort.existsPersonaById(personaId)
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.PERSONA_NO_EXISTE)))
                .then();
    }

    private Mono<Void> validarCruceFechas(Long personaId, List<Long> nuevosBootcampIds) {
        return persistencePort.findBootcampIdsByPersona(personaId)
                .collectList()
                .flatMap(bootcampIdsActuales ->
                        Mono.zip(
                                bootcampQueryPort.obtenerBootcampsPorIds(nuevosBootcampIds).collectList(),
                                bootcampQueryPort.obtenerBootcampsPorIds(bootcampIdsActuales).collectList()
                        )
                )
                .flatMap(tuple -> {
                    List<BootcampResumen> nuevos = tuple.getT1();
                    List<BootcampResumen> actuales = tuple.getT2();

                    for (var nuevo : nuevos) {
                        LocalDate inicioNuevo = nuevo.fechaLanzamiento();
                        LocalDate finNuevo = inicioNuevo.plusWeeks(nuevo.duracion());

                        for(var actual : actuales) {
                            LocalDate inicioActual = actual.fechaLanzamiento();
                            LocalDate finActual = inicioActual.plusWeeks(actual.duracion());

                            if (hayCruce(inicioNuevo, finNuevo, inicioActual, finActual)) {
                                return Mono.error(new BusinessException(TechnicalMessage.CRUCE_BOOTCAMPS));
                            }
                        }
                    }

                    return Mono.empty();
                });
    }

    private boolean hayCruce(LocalDate inicio1, LocalDate fin1, LocalDate inicio2, LocalDate fin2) {
        return !inicio1.isAfter(fin2) && !inicio2.isAfter(fin1);
    }
}
