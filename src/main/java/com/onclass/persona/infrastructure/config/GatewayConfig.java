package com.onclass.persona.infrastructure.config;

import com.onclass.persona.domain.spi.BootcampQueryPort;
import com.onclass.persona.infrastructure.adapters.gateway.BootcampWebClientAdapter;
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
    WebClient bootcampWebClient(@Value("${services.bootcamps.base-url}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
