package com.homeenergytracker.api_gateway.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.addRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;

@Configuration
public class DeviceServiceRoutes {

    @Value("${device.service.base-url}")
    private String baseUrl;

    @Bean
    public RouterFunction<ServerResponse> deviceRoutes() {
        return route("device-service")
                .route(RequestPredicates.path("/api/v1/device/**")
                        .and(RequestPredicates.headers(headers -> headers.firstHeader("X-Client-Type") != null)),
                        http())
                .before(uri(baseUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("deviceServiceCircuitBreaker",
                URI.create("forward:/api/v1/fallback/device")))
                .before(addRequestHeader("X-Gateway-Processed", "true"))
                .build();
    }
}
