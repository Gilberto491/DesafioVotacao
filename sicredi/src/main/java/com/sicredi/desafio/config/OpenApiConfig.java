package com.sicredi.desafio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Votação (Sicredi)",
                version = "v1",
                description = "Endpoints para pauta, sessão e votos.",
                contact = @Contact(name = "Gilberto Fredes", url = "https://github.com/gilberto491")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local")
        }
)
@Configuration
public class OpenApiConfig {
}
