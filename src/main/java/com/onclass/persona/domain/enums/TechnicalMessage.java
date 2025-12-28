package com.onclass.persona.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TechnicalMessage {

    TECNOLOGIA_CREADA("201", "Tecnología registrada correctamente", ""),
    CAPACIDAD_CREADA("201", "Capacidad registrada correctamente", ""),
    PERSONA_NO_EXISTE("404", "Persona no existe", "personaId"),
    MAXIMO_BOOTCAMPS("400", "No puedes inscribirte a mas de 5 bootcamps", "bootcampId"),
    CRUCE_BOOTCAMPS("400", "Existe cruce entre bootcamps", "bootcampId"),
    INTERNAL_ERROR("500", "Error interno", "");

    private final String code;
    private final String message;
    private final String param;
}
