package com.ecol.apiGateway.config.swaggerAggregatorConfig.swaggerAggregatorConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties.SwaggerUrl;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import java.util.Set;
import java.util.LinkedHashSet;

@Configuration
public class SwaggerAggregatorConfig {

    @Value("${api.version}")
    private String apiVersion;
    @Bean
    public SwaggerUiConfigProperties swaggerUiConfigProperties(
            RouteDefinitionLocator routeDefinitionLocator
    ) {
        SwaggerUiConfigProperties properties = new SwaggerUiConfigProperties();

        Set<SwaggerUrl> urls = new LinkedHashSet<>();
        routeDefinitionLocator.getRouteDefinitions()
                .filter(route -> route.getId() != null
                        && !route.getId().startsWith("ReactiveCompositeDiscoveryClient"))
                .subscribe(route -> {
                    String serviceId = route.getId();
                    urls.add(new SwaggerUrl(
                            serviceId,
                            "/v3/api-docs/" + serviceId,
                            serviceId.replace("-", " ")
                                    .toUpperCase()
                    ));
                });

        properties.setUrls(urls);
        return properties;
    }

    @Bean
    public GroupedOpenApi gatewayApi() {
        return GroupedOpenApi.builder()
                .group("api-gateway")
                .pathsToMatch("/**")
                .build();
    }
}