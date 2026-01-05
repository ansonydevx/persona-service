package com.onclass.persona.infrastructure.entrypoints.router;

import com.onclass.persona.infrastructure.entrypoints.handler.PersonaHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/personas/{id}/inscripciones",
                    method = RequestMethod.POST,
                    beanClass = PersonaHandler.class,
                    beanMethod = "inscribirse"
            ),
            @RouterOperation(
                    path = "/personas/bootcamps/{id}",
                    method = RequestMethod.GET,
                    beanClass = PersonaHandler.class,
                    beanMethod = "obtenerPersonasPorBootcamp"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(PersonaHandler handler) {
        return RouterFunctions.route()
                .POST("/personas/{id}/inscripciones", handler::inscribirse)
                .GET("/personas/bootcamps/{id}", handler::obtenerPersonasPorBootcamp)
                .build();
    }
}
