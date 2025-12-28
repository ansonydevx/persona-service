package com.onclass.persona.infrastructure.entrypoints.dto;

import java.time.LocalDate;

public record BootcampResumen(
        Long id,
        LocalDate fechaLanzamiento,
        Integer duracion
) {}
