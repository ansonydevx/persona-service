package com.onclass.persona.it;

import com.onclass.persona.domain.spi.BootcampQueryPort;
import com.onclass.persona.domain.spi.ReporteCommandPort;
import com.onclass.persona.infrastructure.adapters.persistence.PersonaEntity;
import com.onclass.persona.infrastructure.adapters.persistence.repository.InscripcionRepository;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class PersonaIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @MockitoBean
    private BootcampQueryPort bootcampQueryPort;

    @MockitoBean
    private ReporteCommandPort reporteCommandPort;

    @BeforeEach
    void setUp() {
        inscripcionRepository.deleteAll().block();
        personaRepository.deleteAll().block();

        personaRepository.save(
                new PersonaEntity(1L, "Juan", "juan@test.com")
        ).block();

        when(reporteCommandPort.incrementarPersonas(anyList()))
                .thenReturn(Mono.empty());
    }
}
