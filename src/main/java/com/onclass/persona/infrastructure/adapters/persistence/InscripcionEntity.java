package com.onclass.persona.infrastructure.adapters.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("inscripciones")
@Getter
@Setter
@AllArgsConstructor
public class InscripcionEntity {
    private Long personaId;
    private Long bootcampId;
}
