package com.homeenergytracker.api_gateway.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class InsightServiceRoutes {

    @Value("${insight.service.base-url}")
    private String baseUrl;

    // Route 1: Only POST requests to /insight/report with Authorization header are
    // proxied
    @Bean
    public RouterFunction<ServerResponse> insightReportRoutes() {
        return route("insight-report-service")
                .route(
                        RequestPredicates.path("/api/v1/insight/report")
                                .and(RequestPredicates.method(HttpMethod.POST))
                                .and(RequestPredicates
                                        .headers(headers -> headers.firstHeader("Authorization") != null)),
                        http())
                .before(uri(baseUrl))
                .build();
    }

    // Route 2: General wildcard route for all other insight endpoints
    @Bean
    public RouterFunction<ServerResponse> insightRoutes() {
        return route("insight-service")
                .route(RequestPredicates.path("/api/v1/insight/**"), http())
                .before(uri(baseUrl))
                .build();
    }

    // Route 3: Catch-all fallback — if someone sends a GET to /insight/report,
    // it won't match Route 1 (wrong method). It falls through to here and gets a
    // 405.
    @Bean
    public RouterFunction<ServerResponse> insightFallbackRoute() {
        return RouterFunctions.route(
                RequestPredicates.path("/api/v1/insight/report"),
                request -> ServerResponse
                        .status(HttpStatus.METHOD_NOT_ALLOWED)
                        .header("Allow", "POST")
                        .body("Only POST requests are accepted on this endpoint."));
    }
}
