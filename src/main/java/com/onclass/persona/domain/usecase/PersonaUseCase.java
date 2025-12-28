package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.enums.TechnicalMessage;
import com.onclass.persona.domain.exceptions.BusinessException;
import com.onclass.persona.domain.api.PersonaServicePort;
import com.onclass.persona.domain.spi.BootcampQueryPort;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
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

    public PersonaUseCase(
            PersonaPersistencePort persistencePort,
            BootcampQueryPort bootcampQueryPort
    ) {
        this.persistencePort = persistencePort;
        this.bootcampQueryPort = bootcampQueryPort;
    }

    @Override
    public Mono<Void> inscribirse(Long personaId, List<Long> bootcampIds) {
        if (bootcampIds == null || bootcampIds.isEmpty()) {
            return Mono.empty();
        }

        if (bootcampIds.size() > 5) {
            return Mono.error(new BusinessException(TechnicalMessage.MAXIMO_BOOTCAMPS));
        }

        return persistencePort.existsPersonaById(personaId)
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? validarInscripcion(personaId, bootcampIds)
                        : Mono.error(new BusinessException(TechnicalMessage.PERSONA_NO_EXISTE))
                );
    }

    private Mono<Void> validarInscripcion(Long personaId, List<Long> nuevosBootcampIds) {
        return persistencePort.countInscripciones(personaId)
                .flatMap(count -> count + nuevosBootcampIds.size() > 5
                        ? Mono.error(
                                new BusinessException(
                                        TechnicalMessage.MAXIMO_BOOTCAMPS))
                        : validarCruceFechas(personaId, nuevosBootcampIds)
                );
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

                    return persistencePort
                            .saveInscripciones(personaId, nuevosBootcampIds);
                });
    }

    private boolean hayCruce(LocalDate inicio1, LocalDate fin1, LocalDate inicio2, LocalDate fin2) {
        return !inicio1.isAfter(fin2) && !inicio2.isAfter(fin1);
    }
}
