package com.ecol.apiGateway.filter.RateLimitingFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitExceededFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.defer(() -> {

            if (exchange.getResponse().getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                String ip = exchange.getRequest().getRemoteAddress() != null
                        ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                        : "unknown";

                log.warn("Rate limit exceeded for IP [{}] on path: {}",
                        ip, exchange.getRequest().getURI().getPath());

                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                String body = """
                        {
                          "timestamp": "%s",
                          "status": 429,
                          "error": "Too Many Requests",
                          "message": "You have exceeded the 2000 requests per minute limit. Please slow down.",
                          "path": "%s",
                          "retryAfter": "60 seconds"
                        }
                        """.formatted(
                        Instant.now(),
                        exchange.getRequest().getURI().getPath()
                );

                DataBuffer buffer = exchange.getResponse()
                        .bufferFactory()
                        .wrap(body.getBytes(StandardCharsets.UTF_8));

                return exchange.getResponse().writeWith(Mono.just(buffer));
            }

            return Mono.empty();
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}