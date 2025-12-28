package com.onclass.persona.infrastructure.entrypoints.dto;

import java.util.List;

public record IdsRequest(
        List<Long> ids
) {}
