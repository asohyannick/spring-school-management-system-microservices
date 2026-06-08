package com.ecol.apiGateway.exception.globalExceptionHandler;
import com.ecol.apiGateway.exception.gatewayErrorResponse.GatewayErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = resolveStatus(ex);
        String message   = resolveMessage(ex);

        log.error("Gateway error [{} {}]: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI().getPath(),
                ex.getMessage()
        );

        GatewayErrorResponse errorResponse = GatewayErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(exchange.getRequest().getURI().getPath())
                .method(exchange.getRequest().getMethod().name())
                .build();

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

    private HttpStatus resolveStatus(Throwable ex) {
        if (ex instanceof ResponseStatusException rse) {
            return HttpStatus.valueOf(rse.getStatusCode().value());
        }
        if (ex instanceof io.jsonwebtoken.ExpiredJwtException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (ex instanceof io.jsonwebtoken.JwtException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (ex instanceof org.springframework.web.server.MethodNotAllowedException) {
            return HttpStatus.METHOD_NOT_ALLOWED;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveMessage(Throwable ex) {
        if (ex instanceof io.jsonwebtoken.ExpiredJwtException) {
            return "Your session has expired. Please log in again.";
        }
        if (ex instanceof io.jsonwebtoken.JwtException) {
            return "Invalid or malformed authentication token.";
        }
        if (ex instanceof ResponseStatusException rse) {
            return rse.getReason() != null ? rse.getReason() : rse.getMessage();
        }
        return "An unexpected error occurred at the gateway level.";
    }
}