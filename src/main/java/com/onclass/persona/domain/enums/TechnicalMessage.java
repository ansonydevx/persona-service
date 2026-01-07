package com.onclass.persona.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    PERSONA_NO_EXISTE("404", "La persona no existe en la base de datos", "personaId"),
    MAXIMO_BOOTCAMPS("400", "No puedes inscribirte a mas de 5 bootcamps", "bootcampIds"),
    CRUCE_BOOTCAMPS("400", "Existe cruce entre bootcamps", "bootcampIds"),
    INTERNAL_ERROR("500", "Error interno", "");

    private final String code;
    private final String message;
    private final String param;
}
