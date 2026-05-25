package com.yue.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3 documentation configuration.
 *
 * <p>This class only defines metadata and security schemes. The actual scanning
 * of controllers and DTOs is handled automatically by springdoc-openapi at
 * application startup.
 *
 * <p>Endpoints exposed:
 * <ul>
 *     <li>{@code /v3/api-docs} - OpenAPI 3 spec in JSON
 *     <li>{@code /swagger-ui.html} - interactive Swagger UI
 * </ul>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Clazz LMS API",
                version = "1.0.0",
                description = """
                        REST API for the Clazz Learning Management System.
                        Manages departments, classes, employees, and students
                        for a training-school context.
                        """,
                contact = @Contact(
                        name = "Yue",
                        url = "https://github.com/jiangyue95/clazz-lms"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development")
        },
        // Applies the "bearerAuch" scheme defined below to every endpoint by default.
        // Endpoints that should be public (e.g. /login, /register) override this
        // with @SecurityRequirement(plural, empty) at the controller method level.
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = """
                Shorted-lived JWT access token issued by POST /login.
                Send as: Authorization: Bearer <accessToken>
                
                When the access token expires, obtain a new one via POST /refresh
                using the refresh token (returned in the same /login response).
                """
)
public class OpenApiConfig {
    // Pure metadata class. Annotations do all the work.
}
