package com.homeenergytracker.api_gateway.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;

@Configuration
public class UserServiceRoutes {

    @Value("${user.service.base-url}")
    private String baseUrl;

    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return route("user-service")
                .route(RequestPredicates.path("/api/v1/user/**"), http())
                .before(uri(baseUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("userServiceCircuitBreaker",
                        URI.create("forward:/api/v1/fallback/user")))
                .build();
    }
}
