package com.onclass.persona.infrastructure.config;

import com.onclass.persona.domain.spi.BootcampQueryPort;
import com.onclass.persona.domain.spi.ReporteCommandPort;
import com.onclass.persona.infrastructure.adapters.gateway.BootcampWebClientAdapter;
import com.onclass.persona.infrastructure.adapters.gateway.ReporteWebClientAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {

    @Bean
    BootcampQueryPort bootcampQueryPort(WebClient bootcampWebClient) {
        return new BootcampWebClientAdapter(bootcampWebClient);
    }

    @Bean
    ReporteCommandPort reporteCommandPort(WebClient reporteWebClient) {
        return new ReporteWebClientAdapter(reporteWebClient);
    }

    @Bean
    WebClient bootcampWebClient(@Value("${services.bootcamps.base-url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    WebClient reporteWebClient(@Value("${services.reportes.base-url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
