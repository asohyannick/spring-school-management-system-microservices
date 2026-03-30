package com.ecol.userService.config.swaggerConfig;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfig {
    public OpenAPI userService() {
        return  new OpenAPI().addTagsItem(new Tag()
                .name("Authentication and Authorization Service")
                .description("Endpoints for registration, login, verification, password reset, and token management.")
        );
    }
}
