package com.ecol.apiGateway.config.securityConfig;
import com.ecol.apiGateway.config.corsConfig.CorsConfig;
import com.ecol.apiGateway.filter.gatewayAccessDeniedHandler.GatewayAccessDeniedHandler;
import com.ecol.apiGateway.filter.gatewayAuthEntryPoint.GatewayAuthenticationEntryPoint;
import com.ecol.apiGateway.filter.jwtAuthFilter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Value("${api.version}")
    private String apiVersion;

    private String[] publicEndpoints() {
        String base = "/api/" + apiVersion + "/auth";
        return new String[] {
                base + "/register",
                base + "/login",
                base + "/logout",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/error"
        };
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(auth -> auth
                        .pathMatchers(publicEndpoints()).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationFilter,
                       SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new GatewayAuthenticationEntryPoint())
                        .accessDeniedHandler(new GatewayAccessDeniedHandler())
                )
                .build();
    }
}