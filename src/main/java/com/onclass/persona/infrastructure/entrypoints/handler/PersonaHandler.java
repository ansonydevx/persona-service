package com.onclass.persona.infrastructure.entrypoints.handler;

import com.onclass.persona.domain.api.PersonaServicePort;
import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.infrastructure.entrypoints.dto.InscripcionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonaHandler {

    private final PersonaServicePort personaServicePort;

    public Mono<ServerResponse> inscribirse(ServerRequest request) {
        Long personaId = Long.parseLong(request.pathVariable("id"));

        return request.bodyToMono(InscripcionRequest.class)
                .flatMap(dto ->
                        personaServicePort.inscribirse(
                                personaId,
                                dto.bootcampIds()
                        )
                )
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> obtenerPersonasPorBootcamp(ServerRequest request) {
        Long bootcampId = Long.parseLong(request.pathVariable("id"));

        return ServerResponse.ok()
                .body(
                        personaServicePort.obtenerPersonasPorBootcamp(bootcampId),
                        Persona.class
                );
    }
}
