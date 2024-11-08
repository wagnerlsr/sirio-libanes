package br.com.sirio.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =@Info(
                title = "User API",
                version = "1.0",
                contact = @Contact(
                        name = "Sirio", email = "user-apis@sirio.com.br", url = "https://www.sirio.com.br"
                ),
                license = @License(
                        name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
                ),
                termsOfService = "https://www.sirio.com.br/termos",
                description = "Sirio Libânes"
        ),
        servers = @Server(
                url = "http://localhost:3000",
                description = "Test"
        )
)public class OpenAPISecurityConfiguration {
}
