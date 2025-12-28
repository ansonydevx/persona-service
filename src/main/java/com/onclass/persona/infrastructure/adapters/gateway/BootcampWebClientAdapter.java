package com.onclass.persona.infrastructure.adapters.gateway;

import com.onclass.persona.domain.spi.BootcampQueryPort;
import com.onclass.persona.infrastructure.entrypoints.dto.BootcampResumen;
import com.onclass.persona.infrastructure.entrypoints.dto.IdsRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BootcampWebClientAdapter implements BootcampQueryPort {
    private final WebClient webClient;

    @Override
    public Flux<BootcampResumen> obtenerBootcampsPorIds(List<Long> ids) {
        return webClient.post()
                .uri("/bootcamps/by-ids")
                .bodyValue(new IdsRequest(ids))
                .retrieve()
                .bodyToFlux(BootcampResumen.class);
    }
}
