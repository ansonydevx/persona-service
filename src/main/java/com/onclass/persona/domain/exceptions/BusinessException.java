package com.onclass.persona.domain.exceptions;

import com.onclass.persona.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final TechnicalMessage technicalMessage;

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage.toString());
        this.technicalMessage = technicalMessage;
    }
}
