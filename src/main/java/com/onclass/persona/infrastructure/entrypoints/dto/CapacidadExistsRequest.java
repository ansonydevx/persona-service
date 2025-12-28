package com.onclass.persona.infrastructure.entrypoints.dto;

import java.util.List;

public record CapacidadExistsRequest(
        List<Long> ids
) { }
