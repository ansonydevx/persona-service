package com.onclass.persona.domain.spi;

import com.onclass.persona.infrastructure.entrypoints.dto.BootcampResumen;
import reactor.core.publisher.Flux;

import java.util.List;

public interface BootcampQueryPort {
    Flux<BootcampResumen> obtenerBootcampsPorIds(List<Long> bootcampIds);
}
