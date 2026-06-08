package com.ecol.apiGateway.filter.requestLoggingFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter implements WebFilter, Ordered {

    private static final String REQUEST_ID_HEADER  = "X-Request-ID";
    private static final String START_TIME_ATTR    = "requestStartTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String requestId = request.getHeaders().containsKey(REQUEST_ID_HEADER)
                ? request.getHeaders().getFirst(REQUEST_ID_HEADER)
                : UUID.randomUUID().toString();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(r -> r.header(REQUEST_ID_HEADER, requestId))
                .response(exchange.getResponse())
                .build();

        mutatedExchange.getResponse()
                .getHeaders()
                .add(REQUEST_ID_HEADER, requestId);

        mutatedExchange.getAttributes().put(START_TIME_ATTR, Instant.now());

        logIncomingRequest(request, requestId);

        return chain.filter(mutatedExchange)
                .doFinally(signalType -> logOutgoingResponse(mutatedExchange, requestId));
    }

    private void logIncomingRequest(ServerHttpRequest request, String requestId) {
        String clientIp = resolveClientIp(request);

        log.info("""
                ┌─── Incoming Request ────────────────────────────
                │ Request ID : {}
                │ Method     : {}
                │ Path       : {}
                │ Client IP  : {}
                │ User-Agent : {}
                │ Query      : {}
                └─────────────────────────────────────────────────""",
                requestId,
                request.getMethod(),
                request.getURI().getPath(),
                clientIp,
                request.getHeaders().getFirst("User-Agent"),
                request.getURI().getQuery() != null ? request.getURI().getQuery() : "none"
        );
    }

    private void logOutgoingResponse(ServerWebExchange exchange, String requestId) {
        ServerHttpResponse response  = exchange.getResponse();
        Instant startTime            = exchange.getAttribute(START_TIME_ATTR);
        long durationMs              = startTime != null
                ? Duration.between(startTime, Instant.now()).toMillis()
                : -1;

        int statusCode = response.getStatusCode() != null
                ? response.getStatusCode().value()
                : 0;

        if (statusCode >= 500) {
            log.error("""
                    ┌─── Outgoing Response ───────────────────────────
                    │ Request ID : {}
                    │ Method     : {}
                    │ Path       : {}
                    │ Status     : {}
                    │ Duration   : {} ms
                    └─────────────────────────────────────────────────""",
                    requestId,
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI().getPath(),
                    statusCode,
                    durationMs
            );
        } else if (statusCode >= 400) {
            log.warn("""
                    ┌─── Outgoing Response ───────────────────────────
                    │ Request ID : {}
                    │ Method     : {}
                    │ Path       : {}
                    │ Status     : {}
                    │ Duration   : {} ms
                    └─────────────────────────────────────────────────""",
                    requestId,
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI().getPath(),
                    statusCode,
                    durationMs
            );
        } else {
            log.info("""
                    ┌─── Outgoing Response ───────────────────────────
                    │ Request ID : {}
                    │ Method     : {}
                    │ Path       : {}
                    │ Status     : {}
                    │ Duration   : {} ms
                    └─────────────────────────────────────────────────""",
                    requestId,
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI().getPath(),
                    statusCode,
                    durationMs
            );
        }
    }

    private String resolveClientIp(ServerHttpRequest request) {
        String xForwardedFor = request.getHeaders().getFirst("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeaders().getFirst("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp.trim();
        }

        return request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : "unknown";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}