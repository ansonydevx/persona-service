package com.onclass.persona.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface ReporteCommandPort {
    Mono<Void> incrementarPersonas(List<Long> bootcampIds);
}
