package com.onclass.persona.infrastructure.adapters.gateway;

import com.onclass.persona.domain.spi.ReporteCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class ReporteWebClientAdapter implements ReporteCommandPort {

    private final WebClient webClient;

    @Override
    public Mono<Void> incrementarPersonas(List<Long> bootcampIds) {
        return Flux.fromIterable(bootcampIds)
                .flatMap(bootcampId ->
                        webClient.post()
                                .uri("/reportes/bootcamps/{id}/incrementar-personas", bootcampId)
                                .retrieve()
                                .bodyToMono(Void.class)
                                .onErrorResume(e -> Mono.empty())

                )
                .then();
    }
}
