package com.ecol.apiGateway.filter.jwtAuthFilter;

import com.ecol.apiGateway.jwt.JwtValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter implements WebFilter {

    private final JwtValidator jwtValidator;

    private static final String BEARER_PREFIX       = "Bearer ";
    private static final String ACCESS_TOKEN_COOKIE = "accessToken";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = resolveToken(exchange);

        if (!StringUtils.hasText(token)) {
            return chain.filter(exchange);
        }

        try {
            Claims claims = jwtValidator.validate(token);
            String email  = claims.getSubject();
            String role   = claims.get("role", String.class);

            if (!StringUtils.hasText(email)) {
                return chain.filter(exchange);
            }

            String authority = "ROLE_" + (StringUtils.hasText(role) ? role : "STUDENT");

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority(authority))
                    );

            log.debug("Gateway authenticated user: {} with role: {}", email, role);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT at gateway: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

        } catch (JwtException e) {
            log.warn("Invalid JWT at gateway: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private String resolveToken(ServerWebExchange exchange) {

        List<HttpCookie> cookies = exchange.getRequest()
                .getCookies()
                .get(ACCESS_TOKEN_COOKIE);

        if (cookies != null && !cookies.isEmpty()) {
            return cookies.get(0).getValue();
        }

        String bearerToken = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }
}